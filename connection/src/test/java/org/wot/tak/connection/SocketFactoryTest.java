package org.wot.tak.connection;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class SocketFactoryTest {

    public SocketFactory createFactory() {
        SocketFactoryConfig config = new SocketFactoryConfig(
                "test.p12",
                "password",
                "test.p12"
                "password",
                false,
                );
        return new SocketFactory(config);
    }

    @Test
    public void testCreateSSLSocket() throws Exception {
        // given
        SocketFactory factory = createFactory();
        // when
        Socket socket = factory.createSSLSocket("example.com", 443);\
        // then
        assertNotNull(socket);
        assertTrue(socket instanceof SSLSocket);
    }

    @Test
    public void testInitializeTestSocketFactory() throws Exception {
        // given
        SocketFactory factory = createFactory();

        // when
        factory.initializeTestSocketFactory();

        // then
        SSLSocketFactory sslFactory = factory.sslFactory;
        assertNotNull(sslFactory);
    }

    @Test
    public void testInitializeSocketFactory() throws Exception {
        // given
        SocketFactory factory = createFactory();

        // when
        factory.initializeSocketFactory();

        // then
        SSLSocketFactory sslFactory = factory.sslFactory;
        assertNotNull(sslFactory);
    }


}