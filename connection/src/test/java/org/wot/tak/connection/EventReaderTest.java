package org.wot.tak.connection;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EventReaderTest {

    @Test
    public void testClosedTag() throws Exception {
        String test = "<xml/>";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals(test, EventReader.readFrom(in));
    }

    @Test
    public void testNoPreamble() throws Exception {
        String test = "<xml></xml>";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals(test, EventReader.readFrom(in));
    }

    @Test
    public void testPreamble() throws Exception {
        String test = "<?xml?><xml></xml>";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals(test, EventReader.readFrom(in));
    }

    @Test
    public void testPreambleNewLine() throws Exception {
        String test = "<?xml?>\n<xml></xml>";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals(test, EventReader.readFrom(in));
    }

    @Test
    public void testTwo() throws Exception {
        String test = "<one></one><two></two>";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals("<one></one>", EventReader.readFrom(in));
        assertEquals("<two></two>", EventReader.readFrom(in));
    }

    @Test
    public void testNewLineTwo() throws Exception {
        String test = "<one></one>\n<two></two>";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals("<one></one>", EventReader.readFrom(in));
        assertEquals("<two></two>", EventReader.readFrom(in));
    }

    @Test
    public void testComplex() throws Exception {
        String test = "\n\n<?x ?>\n\n<one></one>\n\n<?y ?>\n<two></two>\n\n\n";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals("<?x ?>\n\n<one></one>", EventReader.readFrom(in));
        assertEquals("<?y ?>\n<two></two>", EventReader.readFrom(in));
    }

    @Test
    public void testMix() throws Exception {
        String test = "\n\n<?x ?>\n\n<one></one><two  />\n\n\n";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals("<?x ?>\n\n<one></one>", EventReader.readFrom(in));
        assertEquals("<two  />", EventReader.readFrom(in));
    }

    @Test
    public void testWhitespace() throws Exception {
        String test = "\n\n\n\n\n";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertNull(EventReader.readFrom(in));
    }

    @Test
    public void testOneandWhitespace() throws Exception {
        String test = "<one/>\n\n\n\n\n";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals("<one/>", EventReader.readFrom(in));
        assertNull(EventReader.readFrom(in));
        assertNull(EventReader.readFrom(in));
    }

    @Test
    public void testClient() throws Exception {
        String test = "<event version=\"2.0\" uid=\"S-1-5-21-4270384509-2508030920-1516254662-1001\" type=\"a-f-G-U-C-I\" time=\"2023-08-22T01:40:06Z\" start=\"2023-08-22T01:38:12.99Z\" stale=\"2023-08-22T01:44:27.99Z\" how=\"h-g-i-g-o\"><point lat=\"0\" lon=\"0\" hae=\"0\" ce=\"9999999\" le=\"9999999\"/><detail><takv version=\"4.9.0.172\" platform=\"WinTAK-CIV\" os=\"Microsoft Windows 10 Home\" device=\"Apple Inc. MacBookPro16,1\"/><contact callsign=\"MUSIC\" endpoint=\"*:-1:stcp\"/><uid Droid=\"MUSIC\"/><__group name=\"Green\" role=\"Team Member\"/><status battery=\"99\"/><track course=\"0.00000000\" speed=\"0.00000000\"/><_flow-tags_ TAK-Server-c157ed5a69784cc3ab5e2401a5936074=\"2023-08-22T01:40:06Z\"/></detail></event>";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals(test, EventReader.readFrom(in));
    }

    @Test
    public void testServer() throws Exception {
        String test = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<event version=\"2.0\" uid=\"8f6f6794-466d-4404-9b60-557511f9dab1\" type=\"t-x-takp-v\" time=\"2023-08-22T01:33:14Z\" start=\"2023-08-22T01:33:14Z\" stale=\"2023-08-22T01:34:14Z\" how=\"m-g\"><point lat=\"0.0\" lon=\"0.0\" hae=\"0.0\" ce=\"999999\" le=\"999999\"/><detail><TakControl><TakProtocolSupport version=\"1\"/><TakServerVersionInfo serverVersion=\"4.8-RELEASE-45-HEAD\" apiVersion=\"3\"/></TakControl></detail></event>";
        InputStream in = new ByteArrayInputStream(test.getBytes());
        assertEquals(test, EventReader.readFrom(in));
    }
}
