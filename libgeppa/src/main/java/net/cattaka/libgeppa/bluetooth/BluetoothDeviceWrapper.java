
package net.cattaka.libgeppa.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceWrapper implements IBluetoothDevice {
    private BluetoothDevice device;

    public BluetoothDeviceWrapper(BluetoothDevice device) {
        super();
        this.device = device;
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }

    @Override
    public String getAddress() {
        return device.getAddress();
    }

    @Override
    public String getName() {
        return device.getName();
    }

    @Override
    public IBluetoothSocket createRfcommSocketToServiceRecord(UUID uuid) throws IOException {
        return new BluetoothSocketWrapper(device.createRfcommSocketToServiceRecord(uuid));
    }

}
