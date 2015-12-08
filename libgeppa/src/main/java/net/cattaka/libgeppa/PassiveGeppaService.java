
package net.cattaka.libgeppa;

import net.cattaka.libgeppa.binder.PassiveGeppaServiceFuncs;
import net.cattaka.libgeppa.binder.async.PassiveGeppaServiceFuncsAsync;
import net.cattaka.libgeppa.data.ConnectionCode;
import net.cattaka.libgeppa.data.ConnectionState;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.data.PacketWrapper;
import net.cattaka.libgeppa.passive.IPassiveReceiver;
import net.cattaka.libgeppa.thread.ConnectionThread;
import net.cattaka.libgeppa.thread.IConnectionThreadListener;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;
import android.util.SparseIntArray;

public class PassiveGeppaService<T extends IPacket> extends Service implements
        PassiveGeppaServiceFuncs {
    private PassiveGeppaServiceFuncsAsync mAsync = new PassiveGeppaServiceFuncsAsync(this);

    private IConnectionThreadListener<T> mConnectionThreadListener = new IConnectionThreadListener<T>() {
        @Override
        public void onReceive(T packet) {
            onReceivePacket(packet);
        };

        public void onConnectionStateChanged(ConnectionState state, ConnectionCode code) {
            if (state == ConnectionState.CLOSED) {
                stopConnectionThread();
                if (mBindCount == 0) {
                    stopSelf();
                }
            }

            me.onConnectionStateChanged(state);
            if (code == ConnectionCode.DISCONNECTED) {
                // If other devices are alive, It will restart.
                // otherwise ConnectionThread stop.
                startConnectionThread();
            }
        }
    };

    private IPassiveGeppaService.Stub mBinder = new IPassiveGeppaService.Stub() {
        @Override
        public boolean sendPacket(PacketWrapper packet) throws RemoteException {
            return mAsync.sendPacket(packet);
        }

        @Override
        public boolean isConnected() throws RemoteException {
            return mAsync.isConnected();
        }

        @Override
        public ConnectionState getConnectionState() throws RemoteException {
            return mAsync.getConnectionState();
        }

        @Override
        public int registerGeppaServiceListener(IPassiveGeppaServiceListener listner)
                throws RemoteException {
            return mAsync.registerGeppaServiceListener(listner);
        }

        @Override
        public boolean unregisterGeppaServiceListener(int seq) throws RemoteException {
            return mAsync.unregisterGeppaServiceListener(seq);
        }
    };

    private PassiveGeppaService<T> me = this;

    private ConnectionThread<T> mConnectionThread;

    private ConnectionState mLastConnectionState = ConnectionState.UNKNOWN;

    private IPacketFactory<T> mPacketFactory;

    private IPassiveReceiver<T> mPassiveReceiver;

    private int mListenerSeq;

    private SparseArray<IPassiveGeppaServiceListener> mListenerMap;

    private boolean destroyed;

    private int mBindCount = 0;

    public PassiveGeppaService() throws NullPointerException {
        // mBluetoothAdapter = BluetoothAdapterFactory.getDefaultAdapter();
        mListenerSeq = 1;
        mListenerMap = new SparseArray<IPassiveGeppaServiceListener>();
    }

    public void setup(IPacketFactory<T> packetFactory, IPassiveReceiver<T> passiveReceiver) {
        if (packetFactory == null) {
            throw new NullPointerException("packetFactory");
        }
        if (passiveReceiver == null) {
            throw new NullPointerException("passiveReceiver");
        }
        mPacketFactory = packetFactory;
        mPassiveReceiver = passiveReceiver;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mPacketFactory == null || mPassiveReceiver == null) {
            throw new IllegalStateException("setup method did not called.");
        }
        mPassiveReceiver.onCreateService(me, mPacketFactory);
        destroyed = false;
    }

    @Override
    public IBinder onBind(Intent paramIntent) {
        mBindCount++;
        startConnectionThread();
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        mBindCount++;
        startConnectionThread();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBindCount--;
        if (mBindCount == 0 && mConnectionThread == null) {
            stopSelf();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(mBtConnReceiver);

        stopConnectionThread();
        destroyed = true;
    }

    private ConnectionThread<T> createConnectionThread(
            IConnectionThreadListener<T> connectionThreadListener) {
        return mPassiveReceiver.createConnectionThread(connectionThreadListener);
    }

    protected void startConnectionThread() {
        if (!destroyed && mConnectionThread == null) {
            mConnectionThread = createConnectionThread(mConnectionThreadListener);
            if (mConnectionThread != null) {
                try {
                    me.onConnectionStateChanged(ConnectionState.INITIAL);
                    mConnectionThread.startThread();
                } catch (InterruptedException e) {
                    // Do not interrupt to main thread.
                    throw new RuntimeException("Do not interrupt to main thread!");
                }
            } else {
                // BluetoothAdapter is disabled.
            }
        }
    }

    protected void stopConnectionThread() {
        if (mConnectionThread != null) {
            try {
                mConnectionThread.stopThread();
            } catch (InterruptedException e) {
                // Do not interrupt to main thread.
                throw new RuntimeException("Do not interrupt to main thread!");
            }
            mConnectionThread = null;
        }
    }

    protected void onReceivePacket(T packet) {
        SparseIntArray errors = new SparseIntArray();
        PacketWrapper packetWrapper = new PacketWrapper(packet);
        for (int i = 0; i < mListenerMap.size(); i++) {
            int key = mListenerMap.keyAt(i);
            IPassiveGeppaServiceListener listner = mListenerMap.valueAt(i);
            try {
                listner.onReceivePacket(packetWrapper);
            } catch (RemoteException e) {
                errors.put(errors.size(), key);
            }
        }
        for (int i = 0; i < errors.size(); i++) {
            int key = errors.keyAt(i);
            mListenerMap.remove(key);
        }
    };

    protected void onConnectionStateChanged(ConnectionState state) {
        mLastConnectionState = state;

        SparseIntArray errors = new SparseIntArray();
        for (int i = 0; i < mListenerMap.size(); i++) {
            int key = mListenerMap.keyAt(i);
            IPassiveGeppaServiceListener listner = mListenerMap.valueAt(i);
            try {
                listner.onConnectionStateChanged(state);
            } catch (RemoteException e) {
                errors.put(errors.size(), key);
            }
        }
        for (int i = 0; i < errors.size(); i++) {
            int key = errors.keyAt(i);
            mListenerMap.remove(key);
        }
    }

    public IPacketFactory<T> getPacketFactory() {
        return mPacketFactory;
    }

    /** for binder */
    @Override
    public boolean sendPacket(PacketWrapper packet) {
        if (mConnectionThread != null) {
            @SuppressWarnings("unchecked")
            T t = (T)packet.getPacket();
            return mConnectionThread.sendPacket(t);
        }
        return false;
    }

    /** for binder */
    @Override
    public boolean isConnected() {
        return (mLastConnectionState == ConnectionState.CONNECTED);
    }

    /** for binder */
    @Override
    public ConnectionState getConnectionState() {
        return mLastConnectionState;
    }

    /** for binder */
    @Override
    public int registerGeppaServiceListener(IPassiveGeppaServiceListener listner) {
        mListenerMap.put(mListenerSeq, listner);
        return mListenerSeq++;
    }

    /** for binder */
    @Override
    public boolean unregisterGeppaServiceListener(int seq) {
        boolean result = (mListenerMap != null);
        mListenerMap.remove(seq);
        return result;
    }

    @Override
    public IBinder asBinder() {
        // not used
        return null;
    }
}
