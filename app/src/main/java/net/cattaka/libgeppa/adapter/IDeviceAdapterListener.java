
package net.cattaka.libgeppa.adapter;

import net.cattaka.libgeppa.data.DeviceEventCode;
import net.cattaka.libgeppa.data.DeviceInfo;
import net.cattaka.libgeppa.data.DeviceState;
import net.cattaka.libgeppa.data.IPacket;

public interface IDeviceAdapterListener<T extends IPacket> {

    void onDeviceStateChanged(DeviceState state, DeviceEventCode code, DeviceInfo deviceInfo);

    void onReceivePacket(T packet);
}
