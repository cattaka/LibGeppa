
package net.cattaka.libgeppa.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IPacketFactory<T extends IPacket> {
    public T readPacket(InputStream in) throws IOException;

    public void writePacket(OutputStream out, T packet) throws IOException;
}
