package org.wot.tak.connection.connectors;

import lombok.Getter;
import org.awaitility.core.ThrowingRunnable;
import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.protocol.protobuf.DetailOuterClass;
import org.wot.tak.connection.protocol.protobuf.Takmessage;
import org.wot.tak.connection.protocol.xml.Event;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Getter
public class Receiver implements MessageReceiver {

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

    public ThrowingRunnable received(Takmessage.TakMessage message) {
        return () -> {
            var contact = Optional.ofNullable(message.getCotEvent().getDetail().getContact());

            var receivedContacts = protobufMessages.stream()
                    .map(Takmessage.TakMessage::getCotEvent)
                    .map(event -> Optional.ofNullable(event.getDetail()))
                    .map(detail -> detail.map(DetailOuterClass.Detail::getContact))
                    .collect(Collectors.toList());

            assertThat(receivedContacts).contains(contact);
        };
    }
}
