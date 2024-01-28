package org.wot.tak.connection.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.wot.tak.common.Password;
import org.wot.tak.common.UserName;

import java.nio.file.Path;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
public class AuthenticationConfig {
    private final UserName userName;
    private final Password userPassword;
    private final Path trustStorePath;
    private final Password trustStorePassword;
    private final Path keyStorePath;
    private final Password keyStorePassword;
    private final Boolean serverCertVerification;
}
