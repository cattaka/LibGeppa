
package net.cattaka.libgeppa.data;

import android.os.Parcel;
import android.os.Parcelable;

public enum ConnectionState implements Parcelable {
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

    public static final Parcelable.Creator<ConnectionState> CREATOR = new Parcelable.Creator<ConnectionState>() {
        @Override
        public ConnectionState[] newArray(int size) {
            return new ConnectionState[size];
        }

        @Override
        public ConnectionState createFromParcel(Parcel source) {
            return ConnectionState.valueOf(source.readString());
        }
    };
}
