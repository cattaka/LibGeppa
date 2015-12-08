
package net.cattaka.libgeppa;

import net.cattaka.libgeppa.IActiveGeppaServiceListener;
import net.cattaka.libgeppa.data.PacketWrapper;
import net.cattaka.libgeppa.data.DeviceInfo;

interface IActiveGeppaService {

    int registerServiceListener(IActiveGeppaServiceListener listener);

    boolean unregisterServiceListener(int seq);

    void connect(in DeviceInfo deviceInfo);

    void disconnect();

    DeviceInfo getCurrentDeviceInfo();

    /**
     * @see ConnectionThread#sendPacket(net.cattaka.libgeppa.data.IPacket)
     */
    boolean sendPacket(in PacketWrapper packet);
}
