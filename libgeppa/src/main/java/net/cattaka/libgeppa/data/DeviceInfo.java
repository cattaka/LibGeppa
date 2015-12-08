
package net.cattaka.libgeppa.data;

import net.cattaka.libgeppa.Constants;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class DeviceInfo implements Parcelable {
    private static final String KEY_DEVICE_TYPE = "deviceType";

    private static final String KEY_USB_DEVICE_KEY = "usbDeviceKey";

    private static final String KEY_TCP_HOSTNAME = "tcpHostname";

    private static final String KEY_TCP_PORT = "tcpPort";

    private static final String KEY_SUPPORT_CAMERA = "supportCamera";

    public enum DeviceType {
        DUMMY, SERVICE, USB, TCP;
        public static DeviceType valueOfOrdinal(int ordinal) {
            for (DeviceType t : values()) {
                if (t.ordinal() == ordinal) {
                    return t;
                }
            }
            return DUMMY;
        }
    }

    private DeviceType mDeviceType;

    private String mUsbDeviceKey;

    private String mTcpHostName;

    private int mTcpPort;

    private boolean mSupportCamera;

    public static DeviceInfo createDummy(boolean supportCamera) {
        DeviceInfo info = new DeviceInfo();
        info.mDeviceType = DeviceType.DUMMY;
        info.mSupportCamera = supportCamera;
        return info;
    }

    public static DeviceInfo createUsb(String devicekey, boolean supportCamera) {
        DeviceInfo info = new DeviceInfo();
        info.mDeviceType = DeviceType.USB;
        info.mUsbDeviceKey = devicekey;
        info.mSupportCamera = supportCamera;
        return info;
    }

    public static DeviceInfo createTcp(String hostname, int port, boolean supportCamera) {
        DeviceInfo info = new DeviceInfo();
        info.mDeviceType = DeviceType.TCP;
        info.mTcpHostName = hostname;
        info.mTcpPort = port;
        info.mSupportCamera = supportCamera;
        return info;
    }

    private DeviceInfo() {
        super();
    }

    public String getLabel() {
        switch (mDeviceType) {
            case DUMMY:
                return "DUMMY";
            case SERVICE:
                return "SERVICE";
            case USB:
                return "USB:" + mUsbDeviceKey;
            case TCP:
                return "TCP:" + mTcpHostName + ":" + mTcpPort;
            default:
                return "Unknown";
        }
    }

    public DeviceType getDeviceType() {
        return mDeviceType;
    }

    public String getUsbDeviceKey() {
        return mUsbDeviceKey;
    }

    public String getTcpHostName() {
        return mTcpHostName;
    }

    public int getTcpPort() {
        return mTcpPort;
    }

    public boolean isSupportCamera() {
        return mSupportCamera;
    }

    private <T> boolean equalsValue(T s1, T s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            return s1.equals(s2);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DeviceInfo) {
            DeviceInfo d = (DeviceInfo)o;
            return mDeviceType == o && equalsValue(mUsbDeviceKey, d.mUsbDeviceKey)
                    && equalsValue(mTcpHostName, d.mTcpHostName) && mTcpPort == d.mTcpPort;
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DEVICE_TYPE, mDeviceType.ordinal());
        bundle.putString(KEY_USB_DEVICE_KEY, mUsbDeviceKey);
        bundle.putString(KEY_TCP_HOSTNAME, mTcpHostName);
        bundle.putInt(KEY_TCP_PORT, mTcpPort);
        bundle.putBoolean(KEY_SUPPORT_CAMERA, mSupportCamera);
        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator<DeviceInfo> CREATOR = new Parcelable.Creator<DeviceInfo>() {
        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }

        @Override
        public DeviceInfo createFromParcel(Parcel source) {
            DeviceInfo info = new DeviceInfo();
            Bundle bundle = source.readBundle();
            info.mDeviceType = DeviceType.valueOfOrdinal(bundle.getInt(KEY_DEVICE_TYPE, -1));
            info.mUsbDeviceKey = bundle.getString(KEY_USB_DEVICE_KEY);
            info.mTcpHostName = bundle.getString(KEY_TCP_HOSTNAME);
            info.mTcpPort = bundle.getInt(KEY_TCP_PORT, Constants.DEFAULT_SERVER_PORT);
            info.mSupportCamera = bundle.getBoolean(KEY_SUPPORT_CAMERA);
            return info;
        }
    };

}
