package org.wot.tak.connection;

import java.util.Properties;

public class ConnectorFactory {

    Properties prop;

    public ConnectorFactory(Properties prop) {
        this.prop = prop;
    }

    public TAKServerConnector getSSLConnector() throws Exception{
        return new SSLConnector(
                prop.getProperty("tak.server.url"),
                prop.getProperty("tak.server.port"),
                prop.getProperty("cot.responses"),
                new SocketFactory(prop)
        );
    }

    public TAKServerConnector getSSLCredConnector() throws Exception{
        return new SSLCredConnector(
                prop.getProperty("tak.server.url"),
                prop.getProperty("tak.server.port"),
                prop.getProperty("cot.responses"),
                new SocketFactory(prop),
                prop.getProperty("user.name"),
                prop.getProperty("user.password")
        );
    }

    public TAKServerConnector getCredentialsConnector() throws Exception{
        return new CredentialsConnector(
                prop.getProperty("tak.server.url"),
                prop.getProperty("tak.server.port"),
                prop.getProperty("cot.responses"),
                new SocketFactory(prop),
                prop.getProperty("user.name"),
                prop.getProperty("user.password")
        );
    }

    public TAKServerConnector getPlainConnector() throws Exception{
        return new PlainConnector(
                prop.getProperty("tak.server.url"),
                prop.getProperty("tak.server.port"),
                prop.getProperty("cot.responses"),
                new SocketFactory(prop)
        );
    }

    public TAKServerConnector getUDPConnector() throws Exception{
        return new UDPConnector(
                prop.getProperty("tak.server.url"),
                prop.getProperty("tak.server.port")
        );
    }
}
