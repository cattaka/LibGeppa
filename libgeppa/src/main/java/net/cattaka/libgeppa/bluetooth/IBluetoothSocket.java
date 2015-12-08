
package net.cattaka.libgeppa.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IBluetoothSocket {
    public void close() throws IOException;

    public void connect() throws IOException;

    public InputStream getInputStream() throws IOException;

    public OutputStream getOutputStream() throws IOException;

}
