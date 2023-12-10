package org.wot.tak.connection.protocol.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.wot.tak.connection.messages.asserts.xml.CotAssert.assertAuth;

class XmlEventReaderTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "<xml/>",
            "<xml></xml>",
            "<?xml?><xml></xml>",
            "<?xml?>\n<xml></xml>"})
    void readXmlBytes_singleXml_returnsCorrectXmlBytes(String expected) throws Exception {
        var expectedBytes = expected.getBytes();

        var outputStream = new PipedOutputStream();
        var inputStream = new PipedInputStream(outputStream);
        outputStream.write(expectedBytes);

        var actualBytes = XmlEventReader.readXmlBytes(inputStream);

        assertThat(actualBytes).isEqualTo(expectedBytes);
    }

    static Stream<String[]> readXmlBytes_twoXMLs_returnsCorrectXmlBytes_source() {
        return Stream.of(
                new String[]{"<one></one>", "<two></two>"},
                new String[]{"<one></one>", "\n<two></two>"},
                new String[]{"\n\n<?x ?>\n\n<one></one>\n", "\n<?y ?>\n<two></two>\n\n\n"},
                new String[]{"\n\n<?x ?>\n\n<one></one>", "<two  />\n\n\n"}
        );
    }

    @ParameterizedTest
    @MethodSource("readXmlBytes_twoXMLs_returnsCorrectXmlBytes_source")
    void readXmlBytes_twoXMLs_returnsCorrectXmlBytes(String first, String second) throws Exception {
        var both = first + second;
        var bothBytes = both.getBytes();

        var outputStream = new PipedOutputStream();
        var inputStream = new PipedInputStream(outputStream);
        outputStream.write(bothBytes);

        var actualFirst = XmlEventReader.readXmlBytes(inputStream);
        var actualSecond = XmlEventReader.readXmlBytes(inputStream);

        var expectedFirstTrimmed = first.strip();
        var expectedSecondTrimmed = second.strip();
        var actualFirstTrimmed = new String(actualFirst).strip();
        var actualSecondTrimmed = new String(actualSecond).strip();

        assertThat(actualFirstTrimmed).isEqualTo(expectedFirstTrimmed);
        assertThat(actualSecondTrimmed).isEqualTo(expectedSecondTrimmed);
    }

    @Test
    void readXmlBytes_noData_nonBlockingStream_returnsNoBytes() throws Exception {
        var noBytes = new byte[0];
        var stream = new ByteArrayInputStream(noBytes);
        var actualBytes = XmlEventReader.readXmlBytes(stream);

        assertThat(actualBytes).isNull();
    }

    @Test
    void readXmlBytes_noData_blockingStream_blocksUntilXmlAvailable() throws Exception {
        var expected = "<xml></xml>";
        var expectedBytes = expected.getBytes();

        var outputStream = new PipedOutputStream();
        var inputStream = new PipedInputStream(outputStream);

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(100);
                outputStream.write("<xml></xml>".getBytes());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        var actualBytes = XmlEventReader.readXmlBytes(inputStream);

        assertThat(actualBytes).isEqualTo(expectedBytes);
    }

    @Test
    void readXmlBytes_whitespaces_nonBlockingStream_returnsNoBytes() throws Exception {
        var test = "\n\n \n\n \t";
        InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        var actualBytes = XmlEventReader.readXmlBytes(stream);

        assertThat(actualBytes).isNull();
    }

    @Test
    void readXmlBytes_whitespaces_blockingStream_blocksUntilXmlAvailable() throws Exception {
        var whitespaces = "\n\n \n\n \t";
        var xml = "<xml></xml>";
        var both = whitespaces + xml;
        var expectedBytes = both.getBytes();

        var outputStream = new PipedOutputStream();
        var inputStream = new PipedInputStream(outputStream);

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(100);
                outputStream.write(whitespaces.getBytes());
                Thread.sleep(100);
                outputStream.write(xml.getBytes());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        var actualBytes = XmlEventReader.readXmlBytes(inputStream);

        assertThat(actualBytes).isEqualTo(expectedBytes);
    }

    @Test
    void readXmlBytes_clientXml_returnXmlBytes() throws Exception {
        var xml = "<event version='2.0' uid='S-1-5-21-4270384509-2508030920-1516254662-1001' "
                  + "type='a-f-G-U-C-I' time='2023-08-22T01:40:06Z' start='2023-08-22T01:38:12.99Z' "
                  + "stale='2023-08-22T01:44:27.99Z' how='h-g-i-g-o'>\n"
                  + "    <point lat='0' lon='0' hae='0' ce='9999999' le='9999999'/>\n"
                  + "    <detail>\n"
                  + "        <takv version='4.9.0.172' platform='WinTAK-CIV' os='Microsoft Windows 10 Home' "
                  + "device='Apple Inc. MacBookPro16,1'/>\n"
                  + "        <contact callsign='MUSIC' endpoint='*:-1:stcp'/>\n"
                  + "        <uid Droid='MUSIC'/>\n"
                  + "        <__group name='Green' role='Team Member'/>\n"
                  + "        <status battery='99'/>\n"
                  + "        <track course='0.00000000' speed='0.00000000'/>\n"
                  + "        <_flow-tags_ TAK-Server-c157ed5a69784cc3ab5e2401a5936074='2023-08-22T01:40:06Z'/>\n"
                  + "    </detail>\n"
                  + "</event>";

        var xmlBytes = xml.getBytes();

        var outputStream = new PipedOutputStream();
        var inputStream = new PipedInputStream(outputStream);
        outputStream.write(xml.getBytes());

        var actualBytes = XmlEventReader.readXmlBytes(inputStream);

        assertThat(actualBytes).isEqualTo(xmlBytes);
    }

    @Test
    void readXmlBytes_serverXml_returnXmlBytes() throws Exception {
        var xml = "<?xml version='1.0' encoding='UTF-8'?>\r\n"
                  + "<event version='2.0' uid='8f6f6794-466d-4404-9b60-557511f9dab1' "
                  + "type='t-x-takp-v' time='2023-08-22T01:33:14Z' start='2023-08-22T01:33:14Z' "
                  + "stale='2023-08-22T01:34:14Z' how='m-g'>\r\n"
                  + "    <point lat='0.0' lon='0.0' hae='0.0' ce='999999' le='999999'/>\r\n"
                  + "    <detail>\r\n"
                  + "        <TakControl>\r\n"
                  + "            <TakProtocolSupport version='1'/>\r\n"
                  + "            <TakServerVersionInfo serverVersion='4.8-RELEASE-45-HEAD' apiVersion='3'/>\r\n"
                  + "        </TakControl>\r\n"
                  + "    </detail>\r\n"
                  + "</event>";

        var xmlBytes = xml.getBytes();

        var outputStream = new PipedOutputStream();
        var inputStream = new PipedInputStream(outputStream);
        outputStream.write(xml.getBytes());

        var actualBytes = XmlEventReader.readXmlBytes(inputStream);

        assertThat(actualBytes).isEqualTo(xmlBytes);
    }

    @Test
    void readAuthFromXmlProtocol_authMessageBytes_returnsAuthMessage()
            throws Exception {
        var auth = "<?xml version='1.0'?>\n"
                   + "  <auth>\n"
                   + "    <cot username='user' password='pass' "
                   + "uid='S-1-5-21-1650500091-1818354749-1389322093-1000'/>\n"
                   + "  </auth>";

        var authBytes = auth.getBytes(StandardCharsets.UTF_8);

        var outputStream = new PipedOutputStream();
        var inputStream = new PipedInputStream(outputStream);
        outputStream.write(authBytes);

        var authMessage = XmlEventReader.readAuthFromXmlProtocol(inputStream);

        assertAuth(authMessage)
                .hasUsername("user")
                .hasPassword("pass")
                .hasUid("S-1-5-21-1650500091-1818354749-1389322093-1000");
    }
}
