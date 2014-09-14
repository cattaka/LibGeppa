
package net.cattaka.libgeppa.bluetooth;

import java.io.IOException;
import java.util.UUID;

public interface IBluetoothDevice {
    public String getAddress();

    public String getName();

    public IBluetoothSocket createRfcommSocketToServiceRecord(UUID uuid) throws IOException;

}
