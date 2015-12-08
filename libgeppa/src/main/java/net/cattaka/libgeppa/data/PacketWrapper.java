
package net.cattaka.libgeppa.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PacketWrapper implements Parcelable {
    private IPacket mPacket;

    public static final Parcelable.Creator<PacketWrapper> CREATOR = new Parcelable.Creator<PacketWrapper>() {
        public PacketWrapper createFromParcel(Parcel in) {
            return new PacketWrapper(in);
        }

        public PacketWrapper[] newArray(int size) {
            return new PacketWrapper[size];
        }
    };

    public PacketWrapper(IPacket packet) {
        super();
        mPacket = packet;
    }

    public PacketWrapper(Parcel in) {
        mPacket = (IPacket)in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mPacket);
    }

    public IPacket getPacket() {
        return mPacket;
    }

    public void setPacket(IPacket packet) {
        mPacket = packet;
    }

}
