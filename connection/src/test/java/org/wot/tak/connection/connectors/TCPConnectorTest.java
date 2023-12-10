package org.wot.tak.connection.connectors;

import org.wot.tak.common.Port;
import org.wot.tak.connection.configuration.SocketFactoryConfig;
import org.wot.tak.connection.configuration.TAKServerConnector;
import org.wot.tak.connection.infra.SocketFactory;

import java.net.URL;

class TCPConnectorTest extends ConnectorTestTemplate {

    private final URL serverUrl = new URL("https://127.0.0.1");
    private final Port port = Port.of(8999);

    private final SocketFactory socketFactory = new SocketFactory(SocketFactoryConfig.empty());

    TCPConnectorTest() throws Exception {
    }

    @Override
    protected TAKServerConnector createConnector(boolean negotiateProtobufProtocol) {
        return new TCPConnector(serverUrl.getHost(), port.toString(), socketFactory, negotiateProtobufProtocol);
    }
}
