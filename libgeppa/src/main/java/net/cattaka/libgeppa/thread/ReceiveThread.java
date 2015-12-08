
package net.cattaka.libgeppa.thread;

import java.io.IOException;
import java.io.InputStream;

import net.cattaka.libgeppa.Constants;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import android.os.Handler;
import android.util.Log;

public class ReceiveThread<T extends IPacket> {
    private int mReceiveMessageId;

    private int mErrorMessageId;

    private Handler mHandler;

    private InputStream mInputStream;

    private IPacketFactory<T> mPacketFactory;

    private Thread mThread;

    private boolean mBuggyStream = false;

    public ReceiveThread(int receiveMessageId, int errorMessageId, Handler handler,
            InputStream inputStream, IPacketFactory<T> packetFactory) {
        super();
        mReceiveMessageId = receiveMessageId;
        mErrorMessageId = errorMessageId;
        mHandler = handler;
        mInputStream = inputStream;
        mPacketFactory = packetFactory;
    }

    public void startThread(String label) {
        startThread(label, false);
    }

    public void startThread(String label, boolean buggyStream) {
        mBuggyStream = buggyStream;
        mThread = new Thread("ReceiveThread:" + label) {
            @Override
            public void run() {
                super.run();
                IPacket packet;
                try {
                    while ((packet = mPacketFactory.readPacket(mInputStream)) != null) {
                        mHandler.obtainMessage(mReceiveMessageId, packet).sendToTarget();
                    }
                } catch (IOException e) {
                    mHandler.obtainMessage(mErrorMessageId).sendToTarget();
                } finally {
                    closeStream();
                }
            }
        };
        mThread.start();
    }

    public void stopThread() throws InterruptedException {
        closeStream();
        if (!mBuggyStream) {
            mThread.join();
        }
    }

    private synchronized void closeStream() {
        try {
            mInputStream.close();
        } catch (IOException e) {
            Log.w(Constants.TAG, e.getMessage());
        }
    }
}
