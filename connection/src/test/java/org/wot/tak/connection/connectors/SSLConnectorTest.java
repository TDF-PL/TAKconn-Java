package org.wot.tak.connection.connectors;

import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.configuration.SocketFactoryConfig;
import org.wot.tak.connection.configuration.TAKServerConnector;
import org.wot.tak.connection.infra.SocketFactory;

class SSLConnectorTest extends ConnectorTestTemplate {

    private final Url serverUrl = new Url("tak-dev.1gs20.net");
    private final SocketFactoryConfig config = new SocketFactoryConfig(
            "cert-test/truststore-int-ca.p12",
            "atakatak",
            "cert-test/charlie.p12",
            "atakatak",
            true);

    private final SocketFactory socketFactory = new SocketFactory(config);

    private final Port port = Port.of(8089);

    SSLConnectorTest() throws Exception {
    }

    @Override
    protected TAKServerConnector createConnector(boolean negotiateProtobufProtocol) {
        return new SSLConnector(serverUrl, port, socketFactory, negotiateProtobufProtocol);
    }
}
