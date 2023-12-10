package org.wot.tak.connection.configuration;

import lombok.Value;


@SuppressWarnings("RedundantModifiersValueLombok")
@Value
public class SocketFactoryConfig {

    public static SocketFactoryConfig empty() {
        return new SocketFactoryConfig(
                "", "", "", "", false);
        }

    private String trustStorePath;
    private String trustStorePassword;
    private String keyStorePath;
    private String keyStorePassword;
    private Boolean serverCertVerification;
}
