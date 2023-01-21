package org.wot.tak.connection;

import javax.net.ssl.SSLSocket;
import java.io.IOException;

public class SSLConnector extends TCPConnector{

    protected SSLConnector(String url, String port, String responseStoragePath, SocketFactory sFactory) {
        super(url, port, responseStoragePath, sFactory);
    }

    public void connect() throws IOException {
        s = socketFactory.createSSLSocket(url, port);
        SSLSocket sslSocket = (SSLSocket)s;
        
        sslSocket.startHandshake();
        super.connect();
    }

}
