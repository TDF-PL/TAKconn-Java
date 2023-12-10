package org.wot.tak.connection.protocol.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public final class EventMarshalling {

    private EventMarshalling() {
    }

    public static void toStream(Event event, OutputStream outputStream) throws JAXBException {
        var jaxbContext = JAXBContext.newInstance(Event.class);
        var marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(event, outputStream);
    }

    public static byte[] toBytes(Event event) throws JAXBException {
        var context = JAXBContext.newInstance(Event.class);
        var marshaller = context.createMarshaller();
        var outputStream = new ByteArrayOutputStream();
        marshaller.marshal(event, outputStream);
        return outputStream.toByteArray();
    }

    public static String toString(Event event) {
        try {
            var context = JAXBContext.newInstance(Event.class);
            var marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            var stringWriter = new StringWriter();
            marshaller.marshal(event, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            return e.getMessage();
        }
    }

    public static Event fromBytes(byte[] bytes) throws JAXBException {
        var jaxbContext = JAXBContext.newInstance(Event.class);
        var unmarshaller = jaxbContext.createUnmarshaller();
        return (Event) unmarshaller.unmarshal(new ByteArrayInputStream(bytes));
    }
}
