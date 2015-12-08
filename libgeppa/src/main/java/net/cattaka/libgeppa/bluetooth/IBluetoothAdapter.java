
package net.cattaka.libgeppa.bluetooth;

import java.util.Set;

public interface IBluetoothAdapter {
    public boolean isEnabled();

    public Set<IBluetoothDevice> getBondedDevices();
}
