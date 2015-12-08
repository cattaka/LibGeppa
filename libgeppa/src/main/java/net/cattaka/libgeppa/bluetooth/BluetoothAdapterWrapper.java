
package net.cattaka.libgeppa.bluetooth;

import java.util.HashSet;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class BluetoothAdapterWrapper implements IBluetoothAdapter {
    private BluetoothAdapter adapter;

    public BluetoothAdapterWrapper(BluetoothAdapter adapter) {
        super();
        this.adapter = adapter;
    }

    public boolean isEnabled() {
        return adapter.isEnabled();
    }

    @Override
    public Set<IBluetoothDevice> getBondedDevices() {
        Set<IBluetoothDevice> devices = new HashSet<IBluetoothDevice>();
        for (BluetoothDevice rawDevice : adapter.getBondedDevices()) {
            devices.add(new BluetoothDeviceWrapper(rawDevice));
        }

        return devices;
    }
}
