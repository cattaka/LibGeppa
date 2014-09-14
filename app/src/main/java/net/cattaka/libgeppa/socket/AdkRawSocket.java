
package net.cattaka.libgeppa.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.cattaka.libgeppa.IRawSocket;
import android.os.ParcelFileDescriptor;

public class AdkRawSocket implements IRawSocket {
    private ParcelFileDescriptor mParcelFileDescriptor;

    private InputStream mInputStream;

    private OutputStream mOutputStream;

    private boolean connected;

    private String mLabel;

    public AdkRawSocket(ParcelFileDescriptor parcelFileDescriptor, String label) {
        super();
        mLabel = label;
        mParcelFileDescriptor = parcelFileDescriptor;

        FileDescriptor fd = mParcelFileDescriptor.getFileDescriptor();
        mInputStream = new BufferedInputStream(new FileInputStream(fd));
        mOutputStream = new BufferedOutputStream(new FileOutputStream(fd));

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
        try {
            connected = false;
            mParcelFileDescriptor.getFileDescriptor().sync();
            mParcelFileDescriptor.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
}
