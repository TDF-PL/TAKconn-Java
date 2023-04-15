package org.wot.tak.connection;

import org.junit.Test;

import javax.net.ssl.SSLSocket;
import java.net.Socket;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SocketFactoryTest {

    public SocketFactory createFactory() throws Exception {
        SocketFactoryConfig config = new SocketFactoryConfig(
                "cert-test/truststore-int-ca.p12",
                "atakatak",
                "cert-test/charlie.p12",
                "atakatak",
                true
        );
        return new SocketFactory(config);
    }

    @Test
    public void testCreateSSLSocket() throws Exception {
        // given
        SocketFactory factory = createFactory();
        // when
        Socket socket = factory.createSSLSocket("tak-dev.1gs20.net", 8443);
        // then
        assertNotNull(socket);
        assertTrue(socket instanceof SSLSocket);

        SSLSocket sslSocket = (SSLSocket)socket;
        ((SSLSocket) socket).startHandshake();
    }
}