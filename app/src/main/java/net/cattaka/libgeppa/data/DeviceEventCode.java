
package net.cattaka.libgeppa.data;

import android.os.Parcel;
import android.os.Parcelable;

public enum DeviceEventCode implements Parcelable {
    UNKNOWN, DISCONNECTED, NO_DEVICE, ON_REGISTER;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // StringでParcelに書き出す
        dest.writeString(this.name());
    }

    public static final Parcelable.Creator<DeviceEventCode> CREATOR = new Parcelable.Creator<DeviceEventCode>() {
        @Override
        public DeviceEventCode[] newArray(int size) {
            return new DeviceEventCode[size];
        }

        @Override
        public DeviceEventCode createFromParcel(Parcel source) {
            return DeviceEventCode.valueOf(source.readString());
        }
    };
}
