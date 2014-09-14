
package net.cattaka.libgeppa;

import net.cattaka.libgeppa.bluetooth.BluetoothAdapterFactory;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.passive.BluetoothPassiveReceiver;
import net.cattaka.libgeppa.passive.IPassiveReceiver;
import net.cattaka.libgeppa.passive.IPassiveReceiverListener;

public abstract class BluetoothPassiveGeppaService<T extends IPacket> extends PassiveGeppaService<T> {
    private BluetoothPassiveGeppaService<T> me = this;

    private IPassiveReceiver<T> mPassiveReceiver;

    public BluetoothPassiveGeppaService(String targetDeviceName, IPacketFactory<T> packetFactory)
            throws NullPointerException {
        super();

        if (targetDeviceName == null || packetFactory == null) {
            throw new NullPointerException();
        }

        mPassiveReceiver = new BluetoothPassiveReceiver<T>(new IPassiveReceiverListener() {
            @Override
            public void stopConnectionThread() {
                me.stopConnectionThread();
            }

            @Override
            public void startConnectionThread() {
                me.startConnectionThread();
            }
        }, targetDeviceName, BluetoothAdapterFactory.getDefaultAdapter());

        setup(packetFactory, mPassiveReceiver);
    }
}
