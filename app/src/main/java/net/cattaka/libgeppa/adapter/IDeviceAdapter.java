
package net.cattaka.libgeppa.adapter;

import net.cattaka.libgeppa.data.DeviceInfo;
import net.cattaka.libgeppa.data.DeviceState;
import net.cattaka.libgeppa.data.IPacket;

public interface IDeviceAdapter<T extends IPacket> extends IDeviceCommandAdapter<T> {

    public void startAdapter() throws InterruptedException;

    public void stopAdapter() throws InterruptedException;

    public DeviceState getDeviceState();

    public DeviceInfo getDeviceInfo();
}
