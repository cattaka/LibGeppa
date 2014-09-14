
package net.cattaka.libgeppa.bluetooth;

import android.bluetooth.BluetoothAdapter;

public class BluetoothAdapterFactory {
    public static BluetoothAdapterFactory mFactory = new BluetoothAdapterFactory();

    public static synchronized IBluetoothAdapter getDefaultAdapter() {
        return mFactory.getAdapter();
    }

    public static synchronized void replaceFactory(BluetoothAdapterFactory factory) {
        mFactory = factory;
    }

    public IBluetoothAdapter getAdapter() {
        return new BluetoothAdapterWrapper(BluetoothAdapter.getDefaultAdapter());
    }
}
