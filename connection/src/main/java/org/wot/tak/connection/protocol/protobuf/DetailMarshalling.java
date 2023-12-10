package org.wot.tak.connection.protocol.protobuf;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.experimental.Delegate;
import org.wot.tak.connection.protocol.xml.Detail;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;

public final class DetailMarshalling {

    private DetailMarshalling() {
    }

    public static String toString(Detail detail) throws JAXBException, XMLStreamException {
        var detailQName = new QName(Detail.class.getSimpleName());
        var detailElement = new JAXBElement<>(detailQName, Detail.class, detail);

        var jaxbContext = JAXBContext.newInstance(Detail.class);
        var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        var stringWriter = new StringWriter();
        var xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
        var protobufDetailsWriter = new OutermostTagOmittingWriter(xmlStreamWriter);

        marshaller.marshal(detailElement, protobufDetailsWriter);

        return stringWriter.toString();
    }

    public static Detail fromString(String detail) throws JAXBException {
        var enrichedXml = MessageFormat.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Detail>{0}</Detail>", detail);

        var jaxbContext = JAXBContext.newInstance(Detail.class);
        var unmarshaller = jaxbContext.createUnmarshaller();

        var reader = new StringReader(enrichedXml);
        var source = new StreamSource(reader);
        var detailElement = unmarshaller.unmarshal(source, Detail.class);

        return detailElement.getValue();
    }

    static final class OutermostTagOmittingWriter implements XMLStreamWriter {

        @Delegate(excludes = Exclusions.class)
        private final XMLStreamWriter delegate;
        private int depth = 0;

        OutermostTagOmittingWriter(XMLStreamWriter delegate) {
            this.delegate = delegate;
        }

        @SuppressWarnings("unused") // used by lombok
        public interface Exclusions {
            void writeStartElement(String localName) throws XMLStreamException;

            void writeStartElement(String namespaceURI, String localName) throws XMLStreamException;

            void writeStartElement(String prefix, String localName, String namespaceURI)
                    throws XMLStreamException;

            void writeEndElement() throws XMLStreamException;
        }

        @Override
        public void writeStartElement(String localName) throws XMLStreamException {
            if (depth++ > 0) {
                delegate.writeStartElement(localName);
            }
        }

        @Override
        public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
            if (depth++ > 0) {
                delegate.writeStartElement(namespaceURI, localName);
            }
        }

        @Override
        public void writeStartElement(String prefix, String localName, String namespaceURI)
                throws XMLStreamException {
            if (depth++ > 0) {
                delegate.writeStartElement(prefix, localName, namespaceURI);
            }
        }

        @Override
        public void writeEndElement() throws XMLStreamException {
            if (--depth > 0) {
                delegate.writeEndElement();
            }
        }
    }
}
