package wot.tak.connection;

public class ConnectorFactoryConfig {
    private final String cotResponsesPath;
    private final String takServerUrl;
    private final String takServerPort;
    private final String userName;
    private final String userPassword;
    private final String trustStorePath;
    private final String trustStorePassword;
    private final String keyStorePath;
    private final String keyStorePassword;
    private final Boolean serverCertVerification;

    public ConnectorFactoryConfig(String cotResponsesPath, String takServerUrl, String takServerPort, String userName, String userPassword, String trustStorePath, String trustStorePassword, String keyStorePath, String keyStorePassword, Boolean serverCertVerification) {
        this.cotResponsesPath = cotResponsesPath;
        this.takServerUrl = takServerUrl;
        this.takServerPort = takServerPort;
        this.userName = userName;
        this.userPassword = userPassword;
        this.trustStorePath = trustStorePath;
        this.trustStorePassword = trustStorePassword;
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
        this.serverCertVerification = serverCertVerification;
    }

    public String getCotResponsesPath() {
        return cotResponsesPath;
    }

    public String getTakServerUrl() {
        return takServerUrl;
    }

    public String getTakServerPort() {
        return takServerPort;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    protected SocketFactoryConfig getSocketFactoryConfig() {
        return new SocketFactoryConfig(trustStorePath, trustStorePassword, keyStorePath, keyStorePassword, serverCertVerification);
    }
}
