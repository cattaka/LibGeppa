
package net.cattaka.libgeppa.socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.cattaka.libgeppa.IRawSocket;
import net.cattaka.libgeppa.bluetooth.IBluetoothSocket;

public class BtRawSocket implements IRawSocket {
    private IBluetoothSocket mSocket;

    private InputStream mInputStream;

    private OutputStream mOutputStream;

    private boolean connected;

    private String mLabel;

    public BtRawSocket(IBluetoothSocket socket, int outputBufSize, String label) throws IOException {
        mSocket = socket;
        mLabel = label;
        mInputStream = mSocket.getInputStream();
        mOutputStream = new BufferedOutputStream(mSocket.getOutputStream(), outputBufSize);
        connected = true;
    }

    @Override
    public boolean setup() {
        // none
        return true;
    }

    @Override
    public String getLabel() {
        return mLabel;
    }

    @Override
    public InputStream getInputStream() {
        return mInputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    @Override
    public void close() throws IOException {
        connected = false;
        mSocket.close();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
}
