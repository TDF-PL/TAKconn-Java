package org.wot.tak.connection.protocol.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class XmlEventReader {

    private XmlEventReader() {
    }

    public static Event readFromXmlProtocol(InputStream inputStream) throws IOException, JAXBException {
        var bytes = XmlEventReader.readXmlBytes(inputStream);
        return EventMarshalling.fromBytes(bytes);
    }

    public static Event readFromXmlProtocol(byte[] bytes) throws JAXBException {
        return EventMarshalling.fromBytes(bytes);
    }

    public static Auth readAuthFromXmlProtocol(InputStream inputStream) throws IOException, JAXBException {
        var bytes = XmlEventReader.readXmlBytes(inputStream);
        var jaxbContext = JAXBContext.newInstance(Auth.class);
        var unmarshaller = jaxbContext.createUnmarshaller();
        return (Auth) unmarshaller.unmarshal(new ByteArrayInputStream(bytes));
    }

    static byte[] readXmlBytes(InputStream stream) throws IOException {
        var bytes = new ByteArrayOutputStream();
        var end = false;
        var openTags = 0;
        var started = false;
        var last = -1;
        while (!end) {
            var b = stream.read();
            if (b == -1) {
                return null;
            }

            bytes.write(b);
            if (b == '<') {
                openTags++;
                started = true;
            }
            if (b == '>' && last == '/') {
                openTags--;
            }
            if (b == '/' && last == '<') {
                openTags -= 2;
                do {
                    b = stream.read();
                    if (b == -1) {
                        return null;
                    }
                    bytes.write(b);
                } while (b != '>');
            }
            if (b == '?' && last == '<') {
                do {
                    b = stream.read();
                    if (b == -1) {
                        return null;
                    }
                    bytes.write(b);
                } while (b != '<');
            }
            if (openTags == 0 && started) {
                end = true;
            }
            last = b;
        }

        return bytes.toByteArray();
    }
}
