
package net.cattaka.libgeppa.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

public class BluetoothSocketWrapper implements IBluetoothSocket {
    private BluetoothSocket mSocket;

    public BluetoothSocketWrapper(BluetoothSocket socket) {
        super();
        mSocket = socket;
    }

    public void close() throws IOException {
        mSocket.close();
    }

    public void connect() throws IOException {
        mSocket.connect();
    }

    public InputStream getInputStream() throws IOException {
        return mSocket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return mSocket.getOutputStream();
    }
}
