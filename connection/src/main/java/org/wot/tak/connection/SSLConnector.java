package org.wot.tak.connection;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.bouncycastle.est.jcajce.JsseDefaultHostnameAuthorizer;
import org.bouncycastle.est.jcajce.JsseHostnameAuthorizer;

import java.io.IOException;

public class SSLConnector extends TCPConnector{

    protected SSLConnector(String url, String port, String responseStoragePath, SocketFactory sFactory) {
        super(url, port, responseStoragePath, sFactory);
    }

    public void connect() throws IOException {
        s = socketFactory.createSSLSocket(url, port);
        SSLSocket sslSocket = (SSLSocket)s;
        
        sslSocket.startHandshake();
        SSLSession session = sslSocket.getSession();
        if (session == null) {
            throw new SSLException("Cannot verify SSL socket without session");
        }
        JsseHostnameAuthorizer hostnameVerifier = new JsseDefaultHostnameAuthorizer(null);
        if (!hostnameVerifier.verified(url, session)) {
            throw new SSLPeerUnverifiedException("Cannot verify hostname: " + url);
        }
        super.connect();
    }

}
