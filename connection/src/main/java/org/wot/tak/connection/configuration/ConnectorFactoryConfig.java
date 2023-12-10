package org.wot.tak.connection.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PUBLIC)
@RequiredArgsConstructor
public final class ConnectorFactoryConfig {
    private final String takServerUrl;
    private final String takServerPort;
    private final AuthenticationConfig authenticationConfig;

    public String getUserName() {
        return authenticationConfig.getUserName();
    }

    public String getUserPassword() {
        return authenticationConfig.getUserPassword();
    }

    public SocketFactoryConfig getSocketFactoryConfig() {
        return new SocketFactoryConfig(
                authenticationConfig.getTrustStorePath(),
                authenticationConfig.getTrustStorePassword(),
                authenticationConfig.getKeyStorePath(),
                authenticationConfig.getKeyStorePassword(),
                authenticationConfig.getServerCertVerification());
    }
}
