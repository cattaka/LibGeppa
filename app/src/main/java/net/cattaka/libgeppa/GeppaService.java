
package net.cattaka.libgeppa;

import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;

/**
 * @deprecated use {@link BluetoothPassiveGeppaService}
 */
public class GeppaService<T extends IPacket> extends BluetoothPassiveGeppaService<T> {

    public GeppaService(String targetDeviceName, IPacketFactory<T> packetFactory)
            throws NullPointerException {
        super(targetDeviceName, packetFactory);
    }

}
