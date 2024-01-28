package org.wot.tak.connection.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.wot.tak.common.Password;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.common.UserName;

@Getter(AccessLevel.PUBLIC)
@RequiredArgsConstructor
public final class ConnectorFactoryConfig {
    private final Url takServerUrl;
    private final Port takServerPort;
    private final AuthenticationConfig authenticationConfig;

    public UserName getUserName() {
        return authenticationConfig.getUserName();
    }

    public Password getUserPassword() {
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
