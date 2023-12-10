package org.wot.tak.connection.connectors;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;

import org.bouncycastle.est.jcajce.JsseDefaultHostnameAuthorizer;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.infra.SocketFactory;
import org.wot.tak.connection.infra.StreamingTcpConnector;

public class SSLConnector extends StreamingTcpConnector {

    public SSLConnector(Url url, Port port, SocketFactory sFactory) {
        super(url, port, sFactory, true);
    }

    public SSLConnector(Url url, Port port, SocketFactory sFactory, boolean negotiateProtobufProtocol) {
        super(url, port, sFactory, negotiateProtobufProtocol);
    }

    @Override
    public void connect(MessageReceiver handler) throws Exception {
        this.setSocket(getSocketFactory().createSSLSocket(getUrl(), getPort()));
        var sslSocket = (SSLSocket) getSocket();

        sslSocket.startHandshake();
        var session = sslSocket.getSession();
        if (session == null) {
            throw new SSLException("Cannot verify SSL socket without session");
        }
        var hostnameVerifier = new JsseDefaultHostnameAuthorizer(null);
        if (!hostnameVerifier.verified(getUrl().getUrl(), session)) {
            throw new SSLPeerUnverifiedException("Cannot verify hostname: " + getUrl());
        }
        super.connect(handler);
    }

}
