package org.wot.tak.connection.connectors;

import lombok.Getter;
import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.protocol.protobuf.Takmessage;
import org.wot.tak.connection.protocol.xml.Event;

import java.util.concurrent.CopyOnWriteArrayList;

@Getter
class Receiver implements MessageReceiver {

    private final CopyOnWriteArrayList<Event> xmlMessages = new CopyOnWriteArrayList<>();

    private final CopyOnWriteArrayList<Takmessage.TakMessage> protobufMessages = new CopyOnWriteArrayList<>();

    @Override
    public void receiveByXmlProtocol(Event event) {
        xmlMessages.add(event);
    }

    @Override
    public void receiveByProtobufProtocol(Takmessage.TakMessage event) {
        protobufMessages.add(event);
    }
}
