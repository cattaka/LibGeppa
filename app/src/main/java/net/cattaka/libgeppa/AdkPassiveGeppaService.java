
package net.cattaka.libgeppa;

import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.passive.AdkPassiveReceiver;
import net.cattaka.libgeppa.passive.IPassiveReceiver;
import net.cattaka.libgeppa.passive.IPassiveReceiverListener;

public abstract class AdkPassiveGeppaService<T extends IPacket> extends PassiveGeppaService<T> {

    private AdkPassiveGeppaService<T> me = this;

    private IPassiveReceiver<T> mPassiveReceiver;

    public AdkPassiveGeppaService(IPacketFactory<T> packetFactory) throws NullPointerException {
        super();
        if (packetFactory == null) {
            throw new NullPointerException();
        }

        mPassiveReceiver = new AdkPassiveReceiver<T>(new IPassiveReceiverListener() {
            @Override
            public void stopConnectionThread() {
                me.stopConnectionThread();
            }

            @Override
            public void startConnectionThread() {
                me.startConnectionThread();
            }
        });

        setup(packetFactory, mPassiveReceiver);
    }
}
