
package net.cattaka.libgeppa;

import java.util.UUID;

public class Constants {
    public static final String TAG = "LibGeppa";

    public static final String TAG_DEBUG = "LibGeppa.debug";

    public static final int DEFAULT_SERVER_PORT = 5000;

    public static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final byte STX = 0x02;

    public static final byte ETX = 0x03;

    public static final byte FRAMETYPE_ACK = 0x01;

    public static final byte FRAMETYPE_DATA = 0x02;

    public static final byte FRAMETYPE_REQ_SEND = 0x03;

    public static final int OUTPUT_BUF_SIZE = 0x100;
}
