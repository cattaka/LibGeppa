
package net.cattaka.libgeppa.data;

import android.os.Parcel;
import android.os.Parcelable;

public enum ConnectionCode implements Parcelable {
    UNKNOWN, DISCONNECTED, NO_DEVICE;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // StringでParcelに書き出す
        dest.writeString(this.name());
    }

    public static final Parcelable.Creator<ConnectionCode> CREATOR = new Parcelable.Creator<ConnectionCode>() {
        @Override
        public ConnectionCode[] newArray(int size) {
            return new ConnectionCode[size];
        }

        @Override
        public ConnectionCode createFromParcel(Parcel source) {
            return ConnectionCode.valueOf(source.readString());
        }
    };
}
