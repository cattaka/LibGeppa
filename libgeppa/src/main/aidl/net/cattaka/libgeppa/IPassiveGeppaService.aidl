package net.cattaka.libgeppa;

import net.cattaka.libgeppa.data.PacketWrapper;
import net.cattaka.libgeppa.IPassiveGeppaServiceListener;
import net.cattaka.libgeppa.data.ConnectionState;

interface IPassiveGeppaService {
    boolean isConnected();
    ConnectionState getConnectionState();
    boolean sendPacket(in PacketWrapper packet);
    int registerGeppaServiceListener(in IPassiveGeppaServiceListener listner);
    boolean unregisterGeppaServiceListener(in int seq);
}
