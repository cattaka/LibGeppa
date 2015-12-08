
package net.cattaka.libgeppa.passive;

import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.thread.ConnectionThread;
import net.cattaka.libgeppa.thread.IConnectionThreadListener;
import android.content.Context;

public interface IPassiveReceiver<T extends IPacket> {
    public void onCreateService(Context context, IPacketFactory<T> packetFactory);

    public void teardown();

    public ConnectionThread<T> createConnectionThread(
            IConnectionThreadListener<T> connectionThreadListener);
}
