package org.wot.tak.connection.configuration;

import org.wot.tak.connection.connectors.CredentialsConnector;
import org.wot.tak.connection.connectors.TCPConnector;
import org.wot.tak.connection.connectors.SSLConnector;
import org.wot.tak.connection.connectors.SSLCredentialsConnector;
import org.wot.tak.connection.connectors.UDPConnector;
import org.wot.tak.connection.infra.SocketFactory;

public final class ConnectorFactory {

    private final ConnectorFactoryConfig config;

    /**
     * @param configuration Connector configuration.
     */
    public ConnectorFactory(ConnectorFactoryConfig configuration) {
        this.config = configuration;
    }

    /**
     * @return Connector for SSL connection.
     * @throws Exception If an error occurs.
     */
    public TAKServerConnector getSSLConnector() throws Exception {
        return new SSLConnector(
                config.getTakServerUrl(),
                config.getTakServerPort(),
                new SocketFactory(config.getSocketFactoryConfig())
        );
    }

    /**
     * @return Connector for SSL connection with credentials.
     */
    public TAKServerConnector getSSLWithCredentialsConnector() throws Exception {
        return new SSLCredentialsConnector(
                config.getTakServerUrl(),
                config.getTakServerPort(),
                new SocketFactory(config.getSocketFactoryConfig()),
                config.getUserName(),
                config.getUserPassword()
        );
    }

    /**
     * @return Connector for connection using credentials.
     * @throws Exception If an error occurs.
     */
    public TAKServerConnector getCredentialsConnector() throws Exception {
        return new CredentialsConnector(
                config.getTakServerUrl(),
                config.getTakServerPort(),
                new SocketFactory(config.getSocketFactoryConfig()),
                config.getUserName(),
                config.getUserPassword()
        );
    }

    /**
     * @return Connector for plain connection.
     * @throws Exception If an error occurs.
     */
    public TAKServerConnector getPlainConnector() throws Exception {
        return new TCPConnector(
                config.getTakServerUrl(),
                config.getTakServerPort(),
                new SocketFactory(config.getSocketFactoryConfig()));
    }

    /**
     * @return Connector for UDP connection.
     */
    public TAKServerConnector getUDPConnector() {
        return new UDPConnector(
                config.getUserName(),
                config.getUserPassword()
        );
    }
}
