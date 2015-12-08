
package net.cattaka.libgeppa.adapter;

import net.cattaka.libgeppa.data.BaudRate;
import net.cattaka.libgeppa.data.DeviceInfo;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.net.PhysicaloidSocketPrepareTask;
import net.cattaka.libgeppa.thread.ConnectionThread.IRawSocketPrepareTask;
import android.content.Context;
import android.hardware.usb.UsbDevice;

public class LocalDeviceAdapter<T extends IPacket> extends AbsConnectionAdapter<T> {
    private Context mContext;

    private UsbDevice mUsbDevice;

    private BaudRate mBaudRate;

    public LocalDeviceAdapter(IDeviceAdapterListener<T> listener, IPacketFactory<T> packetFactory,
            boolean useMainLooperForListener, Context context, UsbDevice usbDevice,
            BaudRate baudRate) {
        super(listener, packetFactory, useMainLooperForListener);
        mContext = context;
        mUsbDevice = usbDevice;
        mBaudRate = baudRate;
    }

    @Override
    protected IRawSocketPrepareTask createRawSocketPrepareTask() {
        return new PhysicaloidSocketPrepareTask(mContext, mUsbDevice, mBaudRate);
    }

    public UsbDevice getUsbDevice() {
        return mUsbDevice;
    }

    @Override
    public DeviceInfo getDeviceInfo() {
        return DeviceInfo.createUsb(mUsbDevice.getDeviceName(), false);
    }
}
