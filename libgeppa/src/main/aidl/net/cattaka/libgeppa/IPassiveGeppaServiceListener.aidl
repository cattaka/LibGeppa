package net.cattaka.libgeppa;

import net.cattaka.libgeppa.data.PacketWrapper;
import net.cattaka.libgeppa.data.ConnectionState;

interface IPassiveGeppaServiceListener {
    void onConnectionStateChanged(in ConnectionState state);
    void onReceivePacket(in PacketWrapper packet);
}
