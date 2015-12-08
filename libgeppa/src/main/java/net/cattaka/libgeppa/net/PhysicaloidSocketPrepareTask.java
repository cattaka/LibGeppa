
package net.cattaka.libgeppa.net;

import com.physicaloid.lib.Physicaloid;

import net.cattaka.libgeppa.IRawSocket;
import net.cattaka.libgeppa.data.BaudRate;
import net.cattaka.libgeppa.thread.ConnectionThread.IRawSocketPrepareTask;
import android.content.Context;
import android.hardware.usb.UsbDevice;

public class PhysicaloidSocketPrepareTask implements IRawSocketPrepareTask {
    private Context mContext;

    private UsbDevice mUsbDevice;

    private BaudRate mBaudRate;

    public PhysicaloidSocketPrepareTask(Context context, UsbDevice usbDevice, BaudRate baudRate) {
        super();
        mContext = context;
        mUsbDevice = usbDevice;
        mBaudRate = baudRate;
    }

    @Override
    public IRawSocket prepareRawSocket() {
        Physicaloid mPhysicaloid = new Physicaloid(mContext);
        if (mPhysicaloid.open(mBaudRate.getBaud(), mUsbDevice)) {
            return new PhysicaloidSocket(mPhysicaloid);
        } else {
            return null;
        }
    }

}
