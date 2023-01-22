package org.wot.tak.connection;

public class SocketFactoryConfig {
    private final String trustStorePath;
    private final String trustStorePassword;
    private final String keyStorePath;
    private final String keyStorePassword;
    private final Boolean serverCertVerification;

    public SocketFactoryConfig(String trustStorePath, String trustStorePassword, String keyStorePath, String keyStorePassword, Boolean serverCertVerification) {
        this.trustStorePath = trustStorePath;
        this.trustStorePassword = trustStorePassword;
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
        this.serverCertVerification = serverCertVerification;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public Boolean getServerCertVerification() {
        return serverCertVerification;
    }
}
