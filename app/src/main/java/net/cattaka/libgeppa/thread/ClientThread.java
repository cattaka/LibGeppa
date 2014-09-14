
package net.cattaka.libgeppa.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.cattaka.libgeppa.Constants;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import android.util.Log;

public class ClientThread<T extends IPacket> extends Thread {
    public interface IClientThreadListener<T extends IPacket> {
        /**
         * Note : This method is called from ClientReceiveThread, it's not UI
         * thread.
         */
        public void onReceivePacket(ClientThread<T> target, T packet);

        /**
         * Note : This method is called from this thread, it's not UI thread.
         */
        public void onDisconnected(ClientThread<T> target);
    }

    private static class MyEvent {
        private int eventCode;

        private Object data;

        public MyEvent(int eventCode, Object data) {
            super();
            this.eventCode = eventCode;
            this.data = data;
        }
    }

    private static class ClientReceiveThread<T extends IPacket> extends Thread {
        private ClientThread<T> mParent;

        private InputStream mInputStream;

        private IPacketFactory<T> mPacketFactory;

        private IClientThreadListener<T> mListener;

        public ClientReceiveThread(ClientThread<T> parent, InputStream inputStream,
                IPacketFactory<T> packetFactory, IClientThreadListener<T> listener) {
            super("ClientReceiveThread:" + parent);
            mParent = parent;
            mInputStream = inputStream;
            mPacketFactory = packetFactory;
            mListener = listener;
        }

        @Override
        public void run() {
            super.run();
            try {
                while (true) {
                    T packet = mPacketFactory.readPacket(mInputStream);
                    if (packet != null) {
                        mListener.onReceivePacket(mParent, packet);
                    }
                }
            } catch (IOException e) {
                // ignore
            }
            mParent.mEventQueue.add(new MyEvent(0, null));
        }
    }

    private Socket mSocket;

    private IPacketFactory<T> mPacketFactory;

    private IClientThreadListener<T> mListener;

    private BlockingQueue<MyEvent> mEventQueue;

    public ClientThread(Socket socket, IPacketFactory<T> packetFactory,
            IClientThreadListener<T> listener) {
        super("ClientThread:" + socket);
        mSocket = socket;
        mPacketFactory = packetFactory;
        mListener = listener;
        mEventQueue = new LinkedBlockingQueue<ClientThread.MyEvent>();
    }

    @Override
    public void run() {
        super.run();
        ClientReceiveThread<T> receiveThread = null;
        try {
            InputStream in = mSocket.getInputStream();
            OutputStream out = mSocket.getOutputStream();
            { // Creates receiving thread
              // Note: The receiving thread will stop on mSocket.close() in
              // finally block.
                receiveThread = new ClientReceiveThread<T>(this, in, mPacketFactory, mListener);
                receiveThread.start();
            }

            while (true) {
                MyEvent event = mEventQueue.take();
                if (event.eventCode == 0) {
                    break;
                } else if (event.eventCode == 1) {
                    @SuppressWarnings("unchecked")
                    T packet = (T)event.data;
                    mPacketFactory.writePacket(out, packet);
                    out.flush();
                }
            }
        } catch (InterruptedException e) {
            // Impossible
            Log.w(Constants.TAG, e.getMessage(), e);
        } catch (IOException e) {
            // ignore
        } finally {
            mListener.onDisconnected(this);
            try {
                mSocket.close();
            } catch (IOException e) {
                // Impossible
                Log.w(Constants.TAG, e.getMessage(), e);
            }
            if (receiveThread != null) {
                try {
                    receiveThread.join();
                } catch (InterruptedException e) {
                    // Impossible
                    Log.w(Constants.TAG, e.getMessage(), e);
                }
            }
        }
    }

    public void sendPacket(T packet) {
        mEventQueue.add(new MyEvent(1, packet));
    }

    public String getLabel() {
        return String.valueOf(mSocket.getRemoteSocketAddress());
    }

    public void stopThread() {
        mEventQueue.add(new MyEvent(0, null));
        try {
            mSocket.close();
        } catch (IOException e) {
            // Impossible
            Log.w(Constants.TAG, e.getMessage(), e);
        }
        try {
            this.join();
        } catch (InterruptedException e) {
            // Impossible
            Log.w(Constants.TAG, e.getMessage(), e);
        }
    }
}
