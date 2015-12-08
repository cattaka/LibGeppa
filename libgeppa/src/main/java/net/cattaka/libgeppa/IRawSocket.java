
package net.cattaka.libgeppa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IRawSocket {
    public String getLabel();

    public boolean setup();

    public InputStream getInputStream();

    public OutputStream getOutputStream();

    public void close() throws IOException;

    public boolean isConnected();
}
