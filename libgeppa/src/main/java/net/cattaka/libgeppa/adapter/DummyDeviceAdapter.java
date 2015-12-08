
package net.cattaka.libgeppa.adapter;

import net.cattaka.libgeppa.data.DeviceEventCode;
import net.cattaka.libgeppa.data.DeviceInfo;
import net.cattaka.libgeppa.data.DeviceState;
import net.cattaka.libgeppa.data.IPacket;
import android.os.Handler;

public class DummyDeviceAdapter<T extends IPacket> implements IDeviceAdapter<T> {
    private IDeviceAdapterListener<T> mListener;

    private DeviceState mLastDeviceState = DeviceState.INITIAL;

    private static Handler sHandler = new Handler();;

    public DummyDeviceAdapter(IDeviceAdapterListener<T> listener) {
        mListener = listener;
    }

    @Override
    public void startAdapter() throws InterruptedException {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                DeviceInfo deviceInfo = getDeviceInfo();
                mListener.onDeviceStateChanged(DeviceState.CONNECTING, DeviceEventCode.UNKNOWN,
                        deviceInfo);
                mListener.onDeviceStateChanged(DeviceState.CONNECTED, DeviceEventCode.UNKNOWN,
                        deviceInfo);
                mLastDeviceState = DeviceState.CONNECTED;
            }
        });
    }

    @Override
    public void stopAdapter() throws InterruptedException {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                DeviceInfo deviceInfo = getDeviceInfo();
                mListener.onDeviceStateChanged(DeviceState.CLOSED, DeviceEventCode.DISCONNECTED,
                        deviceInfo);
                mLastDeviceState = DeviceState.CLOSED;
            }
        });
    }

    @Override
    public DeviceState getDeviceState() {
        return mLastDeviceState;
    }

    @Override
    public boolean sendPacket(T packet) {
        return true;
    }

    @Override
    public DeviceInfo getDeviceInfo() {
        return DeviceInfo.createDummy(true);
    }
}
