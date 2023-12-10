package org.wot.tak.connection.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
public class AuthenticationConfig {
    private final String userName;
    private final String userPassword;
    private final String trustStorePath;
    private final String trustStorePassword;
    private final String keyStorePath;
    private final String keyStorePassword;
    private final Boolean serverCertVerification;
}
