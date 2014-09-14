
package net.cattaka.libgeppa.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.cattaka.libgeppa.Constants;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.data.SocketState;
import net.cattaka.libgeppa.thread.ClientThread.IClientThreadListener;
import android.os.Handler;
import android.util.Log;

public class ServerThread<T extends IPacket> extends Thread {
    private static final int EVENT_ON_SOCKET_STATE_CHANGED = 1;

    private static final int EVENT_ON_CLIENT_CONNECTED = 2;

    private static final int EVENT_ON_CLIENT_DISCONNECTED = 3;

    private static final int EVENT_ON_RECEIVE_PACKET = 4;

    private static final int EVENT_CLEAN_UP = 5;

    public interface IServerThreadListener<T extends IPacket> {
        public void onClientConnected(ClientThread<T> target);

        public void onClientDisconnected(ClientThread<T> target);

        public void onSocketStateChanged(SocketState socketState);

        public void onReceivePacket(ClientThread<T> from, T packet);
    }

    private static Handler sHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Object[] objs = (Object[])msg.obj;
            ServerThread<?> parent = (ServerThread<?>)objs[0];
            parent.handleMessage(msg);
        };
    };

    private void handleMessage(android.os.Message msg) {
        Object[] objs = (Object[])msg.obj;
        // ServerThread<T> parent = (ServerThread<T>)objs[0];

        if (msg.what == EVENT_ON_SOCKET_STATE_CHANGED) {
            SocketState socketState = (SocketState)objs[1];
            mListener.onSocketStateChanged(socketState);
        } else if (msg.what == EVENT_ON_CLIENT_CONNECTED) {
            @SuppressWarnings("unchecked")
            ClientThread<T> target = (ClientThread<T>)objs[1];
            mClientThreads.add(target);
            mListener.onClientConnected(target);
        } else if (msg.what == EVENT_ON_CLIENT_DISCONNECTED) {
            @SuppressWarnings("unchecked")
            ClientThread<T> target = (ClientThread<T>)objs[1];
            mClientThreads.remove(target);
            mListener.onClientDisconnected(target);
        } else if (msg.what == EVENT_ON_RECEIVE_PACKET) {
            @SuppressWarnings("unchecked")
            ClientThread<T> target = (ClientThread<T>)objs[1];
            @SuppressWarnings("unchecked")
            T packet = (T)objs[2];
            mListener.onReceivePacket(target, packet);
        } else if (msg.what == EVENT_CLEAN_UP) {
            for (ClientThread<T> ct : mClientThreads) {
                ct.stopThread();
            }
        }
    };

    private IClientThreadListener<T> mClientThreadListener = new IClientThreadListener<T>() {

        @Override
        public void onReceivePacket(ClientThread<T> target, T packet) {
            sHandler.obtainMessage(EVENT_ON_RECEIVE_PACKET, new Object[] {
                    me, target, packet
            }).sendToTarget();
        }

        @Override
        public void onDisconnected(ClientThread<T> target) {
            sHandler.obtainMessage(EVENT_ON_CLIENT_DISCONNECTED, new Object[] {
                    me, target
            }).sendToTarget();
        }
    };

    private ServerThread<T> me = this;

    private ServerSocket mServerSocket;

    private int mPort;

    private IPacketFactory<T> mPacketFactory;

    private IServerThreadListener<T> mListener;

    private List<ClientThread<T>> mClientThreads;

    private CountDownLatch mStartLatch;

    public ServerThread(int port, IPacketFactory<T> packetFactory, IServerThreadListener<T> listener) {
        super("ServerThread:" + port);
        mPacketFactory = packetFactory;
        this.mPort = port;
        this.mListener = listener;
        mClientThreads = new LinkedList<ClientThread<T>>();
    }

    @Override
    public void run() {
        try {
            { // Creating SeverSocket
                sHandler.obtainMessage(EVENT_ON_SOCKET_STATE_CHANGED, new Object[] {
                        me, SocketState.INIT
                }).sendToTarget();
                mServerSocket = new ServerSocket(mPort);
                sHandler.obtainMessage(EVENT_ON_SOCKET_STATE_CHANGED, new Object[] {
                        me, SocketState.OPEN
                }).sendToTarget();
            }
            mStartLatch.countDown();
            while (true) {
                Socket socket = mServerSocket.accept();
                ClientThread<T> clientThread = new ClientThread<T>(socket, mPacketFactory,
                        mClientThreadListener);
                clientThread.start();
                sHandler.obtainMessage(EVENT_ON_CLIENT_CONNECTED, new Object[] {
                        me, clientThread
                }).sendToTarget();
            }
        } catch (IOException e) {
            // none
            Log.d(Constants.TAG, e.getMessage(), e);
        } finally {
            sHandler.obtainMessage(EVENT_CLEAN_UP, new Object[] {
                me
            }).sendToTarget();
            sHandler.obtainMessage(EVENT_ON_SOCKET_STATE_CHANGED, new Object[] {
                    me, SocketState.CLOSE
            }).sendToTarget();
            try {
                if (mServerSocket != null && !mServerSocket.isClosed()) {
                    mServerSocket.close();
                }
            } catch (IOException e) {
                // Impossible
                Log.w(Constants.TAG, e.getMessage(), e);
            }
        }
    }

    public void startThread() throws InterruptedException {
        mStartLatch = new CountDownLatch(1);
        start();
        mStartLatch.await();
    }

    public void stopThread() {
        if (isAlive()) {
            try {
                if (!mServerSocket.isClosed()) {
                    mServerSocket.close();
                }
            } catch (IOException e) {
                // Impossible
                Log.w(Constants.TAG, e.getMessage(), e);
            }
        }
    }

    public List<ClientThread<T>> getClientThreads() {
        return mClientThreads;
    }

    public void sendPacket(T packet) {
        for (ClientThread<T> ct : mClientThreads) {
            ct.sendPacket(packet);
        }
    }

}
