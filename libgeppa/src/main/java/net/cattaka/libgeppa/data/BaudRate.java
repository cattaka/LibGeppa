
package net.cattaka.libgeppa.data;

public enum BaudRate {
    BAUD300(300), //
    BAUD600(600), //
    BAUD1200(1200), //
    BAUD2400(2400), //
    BAUD4800(4800), //
    BAUD9600(9600), //
    BAUD14400(14400), //
    BAUD19200(19200), //
    BAUD38400(38400), //
    BAUD57600(57600), //
    BAUD115200(115200), //
    BAUD230400(230400);

    private int baud;

    private BaudRate(int baud) {
        this.baud = baud;
    }

    public int getBaud() {
        return baud;
    }
}
