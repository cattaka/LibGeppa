
package net.cattaka.libgeppa.adapter;

import net.cattaka.libgeppa.data.ConnectionCode;
import net.cattaka.libgeppa.data.ConnectionState;
import net.cattaka.libgeppa.data.DeviceEventCode;
import net.cattaka.libgeppa.data.DeviceInfo;
import net.cattaka.libgeppa.data.DeviceState;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.thread.ConnectionThread;
import net.cattaka.libgeppa.thread.ConnectionThread.IRawSocketPrepareTask;
import net.cattaka.libgeppa.thread.IConnectionThreadListener;
import net.cattaka.libgeppa.util.DeviceUtil;

public abstract class AbsConnectionAdapter<T extends IPacket> implements IDeviceAdapter<T> {
    private ConnectionThread<T> mConnectionThread;

    private IDeviceAdapterListener<T> mListener;

    protected IPacketFactory<T> mPacketFactory;

    private IConnectionThreadListener<T> mConnectionThreadListener = new IConnectionThreadListener<T>() {

        @Override
        public void onReceive(T packet) {
            mListener.onReceivePacket(packet);
        }

        @Override
        public void onConnectionStateChanged(ConnectionState state, ConnectionCode code) {
            DeviceState dState = DeviceUtil.toDeviceState(state);
            DeviceEventCode deCode = DeviceUtil.toDeviceEventCode(code);
            DeviceInfo deviceInfo = getDeviceInfo();
            mListener.onDeviceStateChanged(dState, deCode, deviceInfo);
        }
    };

    public AbsConnectionAdapter(IDeviceAdapterListener<T> listener,
            IPacketFactory<T> packetFactory, boolean useMainLooperForListener) {
        super();
        mListener = listener;
        mPacketFactory = packetFactory;
    }

    abstract protected IRawSocketPrepareTask createRawSocketPrepareTask();

    @Override
    public void startAdapter() throws InterruptedException {
        if (mConnectionThread != null) {
            throw new IllegalStateException("Already running.");
        }
        mConnectionThread = new ConnectionThread<T>(createRawSocketPrepareTask(), mPacketFactory,
                mConnectionThreadListener, true);
        mConnectionThread.startThread();
    }

    @Override
    public void stopAdapter() throws InterruptedException {
        mConnectionThread.stopThread();
    }

    @Override
    public boolean sendPacket(T packet) {
        return mConnectionThread.sendPacket(packet);
    }

    @Override
    public DeviceState getDeviceState() {
        if (mConnectionThread == null) {
            return DeviceState.UNKNOWN;
        }
        return DeviceUtil.toDeviceState(mConnectionThread.getLastConnectionState());
    }

}
