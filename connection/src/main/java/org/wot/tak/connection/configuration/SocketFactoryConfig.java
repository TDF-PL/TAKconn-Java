package org.wot.tak.connection.configuration;

import lombok.Value;
import org.wot.tak.common.Password;

import java.nio.file.Path;

import static org.wot.tak.common.Password.noPassword;
import static org.wot.tak.common.Path.noPath;


@SuppressWarnings("RedundantModifiersValueLombok")
@Value
public class SocketFactoryConfig {

    public static SocketFactoryConfig empty() {
        return new SocketFactoryConfig(
                noPath(), noPassword(), noPath(), noPassword(), false);
        }

    private Path trustStorePath;
    private Password trustStorePassword;
    private Path keyStorePath;
    private Password keyStorePassword;
    private Boolean serverCertVerification;
}
