
package net.cattaka.libgeppa.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;

import net.cattaka.libgeppa.Constants;
import net.cattaka.libgeppa.IRawSocket;
import net.cattaka.libgeppa.data.ConnectionCode;
import net.cattaka.libgeppa.data.ConnectionState;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ConnectionThread<T extends IPacket> {
    private static final int EVENT_RECEIVE = 1;

    private static final int EVENT_SEND = 2;

    private static final int EVENT_SOCKET_CONNECTING = 3;

    private static final int EVENT_SOCKET_CONNECTED = 4;

    private static final int EVENT_SOCKET_CLOSED = 5;

    public interface IRawSocketPrepareTask {
        public IRawSocket prepareRawSocket();
    }

    private Handler.Callback mOuterCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == EVENT_RECEIVE) {
                if (mConnectionThreadListener != null) {
                    @SuppressWarnings("unchecked")
                    T packet = (T)msg.obj;
                    mConnectionThreadListener.onReceive(packet);
                }
                return true;
            } else if (msg.what == EVENT_SOCKET_CONNECTING) {
                mInnerHandler = (Handler)msg.obj;

                mLastConnectionState = ConnectionState.CONNECTING;
                mConnectionThreadListener.onConnectionStateChanged(ConnectionState.CONNECTING,
                        ConnectionCode.UNKNOWN);
                return true;
            } else if (msg.what == EVENT_SOCKET_CONNECTED) {
                mLastConnectionState = ConnectionState.CONNECTED;
                mConnectionThreadListener.onConnectionStateChanged(ConnectionState.CONNECTED,
                        ConnectionCode.UNKNOWN);
                return true;
            } else if (msg.what == EVENT_SOCKET_CLOSED) {
                mLastConnectionState = ConnectionState.CLOSED;
                ConnectionCode code = (ConnectionCode)msg.obj;
                mConnectionThreadListener.onConnectionStateChanged(ConnectionState.CLOSED, code);
                return true;
            }
            return false;
        }
    };

    /**
     * This is used by {@link #mInnerHandler}.
     */
    private class InnerCallback implements Handler.Callback {
        private OutputStream mOutputStream;

        private ReceiveThread<T> mReceiveThread;

        public InnerCallback() {
            super();
        }

        public void setOutputStream(OutputStream outputStream) {
            mOutputStream = outputStream;
        }

        public void setReceiveThread(ReceiveThread<T> receiveThread) {
            mReceiveThread = receiveThread;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == EVENT_RECEIVE) {
                mOuterHandler.obtainMessage(EVENT_RECEIVE, msg.obj).sendToTarget();
                return true;
            } else if (msg.what == EVENT_SEND) {
                @SuppressWarnings("unchecked")
                T packet = (T)msg.obj;
                if (packet != null) {
                    try {
                        mPacketFactory.writePacket(mOutputStream, packet);
                        mOutputStream.flush();
                    } catch (IOException e) {
                        mInnerHandler.obtainMessage(EVENT_SOCKET_CLOSED).sendToTarget();
                    }
                }
                return true;
            } else if (msg.what == EVENT_SOCKET_CLOSED) {
                if (mReceiveThread != null) {
                    try {
                        mReceiveThread.stopThread();
                    } catch (InterruptedException e) {
                        Log.w(Constants.TAG, e.getMessage(), e);
                    }
                    mReceiveThread = null;
                }
                Looper.myLooper().quit();
                return true;
            }
            return false;
        }
    }

    private Thread mThread;

    private IRawSocketPrepareTask mPrepareTask;

    private Handler mOuterHandler;

    private Handler mInnerHandler;

    private IPacketFactory<T> mPacketFactory;

    private IConnectionThreadListener<T> mConnectionThreadListener;

    private ConnectionState mLastConnectionState = ConnectionState.INITIAL;

    private IRawSocket mRawSocket;

    public ConnectionThread(IRawSocketPrepareTask prepareTask, IPacketFactory<T> packetFactory,
            IConnectionThreadListener<T> connectionThreadListener) {
        this(prepareTask, packetFactory, connectionThreadListener, false);
    }

    public ConnectionThread(IRawSocketPrepareTask prepareTask, IPacketFactory<T> packetFactory,
            IConnectionThreadListener<T> connectionThreadListener, boolean useMainLooperForListener) {
        super();
        mPrepareTask = prepareTask;
        mPacketFactory = packetFactory;
        mConnectionThreadListener = connectionThreadListener;

        if (useMainLooperForListener) {
            mOuterHandler = new Handler(Looper.getMainLooper(), mOuterCallback);
        } else {
            mOuterHandler = new Handler(mOuterCallback);
        }

        mRawSocket = mPrepareTask.prepareRawSocket();
    }

    public void startThread() throws InterruptedException {
        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        mThread = new Thread("ConnectionThread:CONNECTING") {
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                InnerCallback innerCallback = new InnerCallback();
                Handler handler = new Handler(innerCallback);
                mOuterHandler.obtainMessage(EVENT_SOCKET_CONNECTING, handler).sendToTarget();

                semaphore.release();
                if (mRawSocket != null && mRawSocket.setup()) {
                    try {
                        this.setName("ConnectionThread:" + mRawSocket.getLabel());
                        InputStream inputStream = mRawSocket.getInputStream();
                        OutputStream outputStream = mRawSocket.getOutputStream();
                        innerCallback.setOutputStream(outputStream);

                        { // Creating receiveThread
                            ReceiveThread<T> receiveThread = new ReceiveThread<T>(EVENT_RECEIVE,
                                    EVENT_SOCKET_CLOSED, handler, inputStream, mPacketFactory);
                            innerCallback.setReceiveThread(receiveThread);
                            receiveThread.startThread(mRawSocket.getLabel());
                        }
                        mOuterHandler.obtainMessage(EVENT_SOCKET_CONNECTED).sendToTarget();

                        Looper.loop();
                    } finally {
                        try {
                            mRawSocket.close();
                        } catch (IOException e) {
                            Log.w(Constants.TAG, e.getMessage());
                        }
                        mOuterHandler.obtainMessage(EVENT_SOCKET_CLOSED,
                                ConnectionCode.DISCONNECTED).sendToTarget();
                    }
                } else {
                    mOuterHandler.obtainMessage(EVENT_SOCKET_CLOSED, ConnectionCode.NO_DEVICE)
                            .sendToTarget();
                }
            }
        };
        mThread.start();
        semaphore.acquire();
        semaphore.release();
    }

    public void stopThread() throws InterruptedException {
        if (mRawSocket != null && !mRawSocket.isConnected()) {
            try {
                mRawSocket.close();
            } catch (IOException e) {
                // Impossible
                Log.w(Constants.TAG, e.getMessage(), e);
            }
        }
        // note : mInnerHandler is exactly not null because it uses semaphore.
        mInnerHandler.obtainMessage(EVENT_SOCKET_CLOSED).sendToTarget();
        mThread.join();
    }

    public boolean sendPacket(T packet) {
        // note : mInnerHandler is exactly not null because it uses semaphore.
        if (mLastConnectionState == ConnectionState.CONNECTED) {
            mInnerHandler.obtainMessage(EVENT_SEND, packet).sendToTarget();
            return true;
        } else {
            return false;
        }
    }

    public ConnectionState getLastConnectionState() {
        return mLastConnectionState;
    }

}
