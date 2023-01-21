package org.wot.tak.connection;

import java.io.IOException;

public class PlainConnector extends TCPConnector{

    protected PlainConnector(String url, String port, String responseStoragePath, SocketFactory sFactory) {
        super(url, port, responseStoragePath, sFactory);
    }

    public void connect() throws IOException {
        s = socketFactory.createSocket(url, port);
        super.connect();
    }

}
