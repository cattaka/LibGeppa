
package net.cattaka.libgeppa.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.cattaka.libgeppa.IRawSocket;

public class DummySocket implements IRawSocket {
    private boolean mConnected;

    private InputStream mInputStream = new InputStream() {
        @Override
        public int read() throws IOException {
            while (mConnected && isConnected()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Impossible
                }
            }
            return -1;
        }

        @Override
        public void close() throws IOException {
            super.close();
            mConnected = false;
        };
    };

    private OutputStream mOutputStream = new OutputStream() {
        @Override
        public void write(int paramInt) throws IOException {
            // none
        }

        @Override
        public void close() throws IOException {
            super.close();
            mConnected = false;
        };
    };

    @Override
    public String getLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setup() {
        mConnected = true;
        return true;
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
        mConnected = false;
    }

    @Override
    public boolean isConnected() {
        return mConnected;
    }

}
