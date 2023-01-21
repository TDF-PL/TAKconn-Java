package wot.tak.connection;

public class ConnectorFactory {

    ConnectorFactoryConfig cfg;

    public ConnectorFactory(ConnectorFactoryConfig cfg) {
        this.cfg = cfg;
    }

    public TAKServerConnector getSSLConnector() throws Exception{
        return new SSLConnector(
                cfg.getTakServerUrl(),
                cfg.getTakServerPort(),
                cfg.getCotResponsesPath(),
                new SocketFactory(cfg.getSocketFactoryConfig())
        );
    }

    public TAKServerConnector getSSLCredConnector() throws Exception{
        return new SSLCredConnector(
                cfg.getTakServerUrl(),
                cfg.getTakServerPort(),
                cfg.getCotResponsesPath(),
                new SocketFactory(cfg.getSocketFactoryConfig()),
                cfg.getUserName(),
                cfg.getUserPassword()
        );
    }

    public TAKServerConnector getCredentialsConnector() throws Exception{
        return new CredentialsConnector(
                cfg.getTakServerUrl(),
                cfg.getTakServerPort(),
                cfg.getCotResponsesPath(),
                new SocketFactory(cfg.getSocketFactoryConfig()),
                cfg.getUserName(),
                cfg.getUserPassword()
        );
    }

    public TAKServerConnector getPlainConnector() throws Exception{
        return new PlainConnector(
                cfg.getTakServerUrl(),
                cfg.getTakServerPort(),
                cfg.getCotResponsesPath(),
                new SocketFactory(cfg.getSocketFactoryConfig())
        );
    }

    public TAKServerConnector getUDPConnector() throws Exception{
        return new UDPConnector(
                cfg.getUserName(),
                cfg.getUserPassword()
        );
    }
}
