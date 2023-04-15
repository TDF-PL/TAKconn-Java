package org.wot.tak.connection;

import org.junit.Test;

import javax.net.ssl.SSLSocket;
import java.net.Socket;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SocketFactoryTest {

    public SocketFactory createFactory() throws Exception {
        SocketFactoryConfig config = new SocketFactoryConfig(
                "test.p12",
                "password",
                "test.p12",
                "password",
                false
        );
        return new SocketFactory(config);
    }

    @Test
    public void testCreateSSLSocket() throws Exception {
        // given
        SocketFactory factory = createFactory();
        // when
        Socket socket = factory.createSSLSocket("example.com", 443);
        // then
        assertNotNull(socket);
        assertTrue(socket instanceof SSLSocket);
    }
}