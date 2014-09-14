
package net.cattaka.libgeppa.adapter;

import net.cattaka.libgeppa.data.DeviceInfo;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.net.RemoteSocketPrepareTask;
import net.cattaka.libgeppa.thread.ConnectionThread.IRawSocketPrepareTask;

public class RemoteDeviceAdapter<T extends IPacket> extends AbsConnectionAdapter<T> {
    private String mHostname;

    private int mPort;

    public RemoteDeviceAdapter(IDeviceAdapterListener<T> listener, IPacketFactory<T> packetFactory,
            boolean useMainLooperForListener, String hostname, int port) {
        super(listener, packetFactory, useMainLooperForListener);
        if (hostname == null) {
            throw new NullPointerException();
        }
        mHostname = hostname;
        mPort = port;
    }

    @Override
    protected IRawSocketPrepareTask createRawSocketPrepareTask() {
        return new RemoteSocketPrepareTask(mHostname, mPort);
    }

    @Override
    public DeviceInfo getDeviceInfo() {
        return DeviceInfo.createTcp(mHostname, mPort, true);
    }
}
