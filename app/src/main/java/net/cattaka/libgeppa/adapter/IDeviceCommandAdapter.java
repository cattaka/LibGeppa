
package net.cattaka.libgeppa.adapter;

import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.thread.ConnectionThread;

public interface IDeviceCommandAdapter<T extends IPacket> {

    /**
     * @see ConnectionThread#sendPacket(net.cattaka.libgeppa.data.IPacket)
     */
    public boolean sendPacket(T packet);
}
