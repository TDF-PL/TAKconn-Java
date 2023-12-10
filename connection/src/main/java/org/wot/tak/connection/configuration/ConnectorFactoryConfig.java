package org.wot.tak.connection.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;

@Getter(AccessLevel.PUBLIC)
@RequiredArgsConstructor
public final class ConnectorFactoryConfig {
    private final Url takServerUrl;
    private final Port takServerPort;
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
