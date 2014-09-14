
package net.cattaka.libgeppa.net;

import net.cattaka.libgeppa.IRawSocket;
import net.cattaka.libgeppa.thread.ConnectionThread.IRawSocketPrepareTask;

public class RemoteSocketPrepareTask implements IRawSocketPrepareTask {
    private String mHostname;

    private int ｍPort;

    public RemoteSocketPrepareTask(String hostname, int port) {
        super();
        mHostname = hostname;
        this.ｍPort = port;
    }

    @Override
    public IRawSocket prepareRawSocket() {
        IRawSocket rawSocket = null;
        rawSocket = new RemoteSocket(mHostname, ｍPort);
        return rawSocket;
    }
}
