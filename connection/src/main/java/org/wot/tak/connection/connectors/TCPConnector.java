package org.wot.tak.connection.connectors;

import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.infra.SocketFactory;
import org.wot.tak.connection.infra.StreamingTcpConnector;

public final class TCPConnector extends StreamingTcpConnector {

    public TCPConnector(Url url, Port port, SocketFactory sFactory) {
        super(url, port, sFactory, true);
    }

    public TCPConnector(Url url, Port port, SocketFactory sFactory, boolean negotiateProtocolVersion) {
        super(url, port, sFactory, negotiateProtocolVersion);
    }

    @Override
    public void connect(MessageReceiver handler) throws Exception {
        this.setSocket(getSocketFactory().createSocket(getUrl(), getPort()));
        super.connect(handler);
    }

}
