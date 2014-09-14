
package net.cattaka.libgeppa.data;

import android.os.Parcel;
import android.os.Parcelable;

public enum DeviceState implements Parcelable {
    UNKNOWN, INITIAL, CONNECTING, CONNECTED, CLOSED;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // StringでParcelに書き出す
        dest.writeString(this.name());
    }

    public static final Parcelable.Creator<DeviceState> CREATOR = new Parcelable.Creator<DeviceState>() {
        @Override
        public DeviceState[] newArray(int size) {
            return new DeviceState[size];
        }

        @Override
        public DeviceState createFromParcel(Parcel source) {
            return DeviceState.valueOf(source.readString());
        }
    };
}
