
package net.cattaka.libgeppa.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.physicaloid.lib.Physicaloid;

import net.cattaka.libgeppa.IRawSocket;
import net.cattaka.libgeppa.exception.NotImplementedException;

public class PhysicaloidSocket implements IRawSocket {
    private class InputStreamEx extends InputStream {
        private byte[] buf = new byte[64];

        private int bufReaded = 0;

        private int bufIdx = 0;

        private boolean closed = false;

        @Override
        public int read() throws IOException {
            while (mPhysicaloid.isOpened() && !closed) {
                if (bufIdx < bufReaded) {
                    return (0xFF) & buf[bufIdx++];
                } else {
                    bufIdx = 0;
                    bufReaded = mPhysicaloid.read(buf);
                }
            }
            return -1;
        }

        @Override
        public int read(byte[] buffer) throws IOException {
            throw new NotImplementedException();
        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            throw new NotImplementedException();
        }

        @Override
        public void close() throws IOException {
            super.close();
            closed = true;
        }
    }

    private class OutputStreamEx extends OutputStream {
        private int mBufIdx;

        private byte[] mBuf = new byte[1 << 12];

        @Override
        public void write(int oneByte) throws IOException {
            mBuf[mBufIdx++] = (byte)oneByte;
            if (mBufIdx == mBuf.length) {
                flush();
            }
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            mPhysicaloid.write(mBuf, mBufIdx);
            mBufIdx = 0;
        }
    }

    private Physicaloid mPhysicaloid;

    private InputStreamEx mInputStream;

    private OutputStreamEx mOutputStream;

    
    public PhysicaloidSocket(Physicaloid ftDriver) {
        super();
        mPhysicaloid = ftDriver;
        mInputStream = new InputStreamEx();
        mOutputStream = new OutputStreamEx();
    }

    @Override
    public boolean setup() {
        return true;
    }

    @Override
    public String getLabel() {
        return "FTDriver Socket";
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
        mPhysicaloid.close();
    }

    @Override
    public boolean isConnected() {
        return mPhysicaloid.isOpened();
    }

}
