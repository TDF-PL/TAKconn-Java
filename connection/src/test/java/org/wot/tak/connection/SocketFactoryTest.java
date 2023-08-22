package org.wot.tak.connection;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class SocketFactoryTest {

    public SocketFactory createFactory() throws Exception {
        SocketFactoryConfig config = new SocketFactoryConfig("cert-test/truststore-int-ca.p12", "atakatak", "cert-test/charlie.p12", "atakatak", true);
        return new SocketFactory(config);
    }

    @Test
    public void testDevServerMASSL() throws Exception {
        // given
        String tmpdir = Files.createTempDirectory("cot").toFile().getAbsolutePath();
        SocketFactory factory = createFactory();
        // when
        TAKServerConnector connector = new SSLConnector("tak-dev.1gs20.net", "8089", tmpdir, factory);
        connector.connect();
        Thread.sleep(2000);
        connector.close();
        // then
        var files = Stream.of(new File(tmpdir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName).collect(Collectors.toList());
        assertTrue(files.size() > 0);
        assertTrue(files.get(0).endsWith("cot"));
        var content = Files.readString(new File(tmpdir, files.get(0)).toPath());
        assertTrue(content.startsWith("<?xml"));
        assertTrue(content.contains("<event"));
        assertTrue(content.contains("TakServerVersionInfo"));
        assertTrue(content.trim().endsWith("</event>"));
    }
}