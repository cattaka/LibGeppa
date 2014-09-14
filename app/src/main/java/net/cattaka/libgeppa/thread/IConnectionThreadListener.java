
package net.cattaka.libgeppa.thread;

import net.cattaka.libgeppa.data.ConnectionCode;
import net.cattaka.libgeppa.data.ConnectionState;
import net.cattaka.libgeppa.data.IPacket;

public interface IConnectionThreadListener<T extends IPacket> {
    public void onConnectionStateChanged(ConnectionState state, ConnectionCode code);

    public void onReceive(T packet);
}
