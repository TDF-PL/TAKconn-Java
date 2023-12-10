package org.wot.tak.connection.configuration;

import jakarta.xml.bind.JAXBException;
import org.wot.tak.connection.protocol.protobuf.Takmessage;
import org.wot.tak.connection.protocol.xml.Event;

import javax.xml.datatype.DatatypeConfigurationException;

public interface MessageReceiver {

        void receiveByXmlProtocol(Event event) throws DatatypeConfigurationException, JAXBException;

        void receiveByProtobufProtocol(Takmessage.TakMessage event);
}
