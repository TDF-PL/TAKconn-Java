package org.wot.tak.connection.connectors;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.wot.tak.connection.UID;
import org.wot.tak.connection.configuration.TAKServerConnector;
import org.wot.tak.connection.messages.ClientProtobufMessage;
import org.wot.tak.connection.messages.ClientXmlMessage;
import org.wot.tak.connection.protocol.ProtocolVersion;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wot.tak.connection.messages.asserts.protobuf.TakMessageAssert.assertThatMessage;
import static org.wot.tak.connection.messages.asserts.xml.EventAssertFluent.assertThatEvent;

abstract class ConnectorTestTemplate {

    private final UID alphaUid = new UID("19022fd5-0b7b-4d98-970e-ae22cdfa7345");
    private final UID bravoUid = new UID("21818631-8f1c-44f0-b45d-aa50183a79af");

    protected abstract TAKServerConnector createConnector(boolean negotiateProtobufProtocol);

    @Test
    void Connector_can_send_and_and_receive_messages_using_XML_protocol() throws Exception {

        var negotiateProtocolVersion = false;
        try (var alphaClient = createConnector(negotiateProtocolVersion);
             var bravoClient = createConnector(negotiateProtocolVersion)) {

            var callsignPattern = "[{0}][{1}][XML] Automated Integration Tests";
            var alphaCallsign = MessageFormat.format(
                    callsignPattern, alphaClient.getClass().getSimpleName(), "Alpha");
            var bravoCallsign = MessageFormat.format(
                    callsignPattern, bravoClient.getClass().getSimpleName(), "Bravo");

            var alphaChatText = MessageFormat.format("Test message {0}", UUID.randomUUID());
            var bravoChatText = MessageFormat.format("Test message {0}", UUID.randomUUID());

            var alphaAnnouncementMessage = ClientXmlMessage.announcement(alphaUid, alphaCallsign);
            var alphaChatMessage = ClientXmlMessage.chat(alphaUid, alphaCallsign, alphaChatText);

            var bravoAnnouncementMessage = ClientXmlMessage.announcement(bravoUid, bravoCallsign);
            var bravoChatMessage = ClientXmlMessage.chat(bravoUid, bravoCallsign, bravoChatText);

            var alphaMessageReceiver = new Receiver();
            var bravoMessageReceiver = new Receiver();

            alphaClient.connect(alphaMessageReceiver);
            bravoClient.connect(bravoMessageReceiver);

            alphaClient.send(alphaAnnouncementMessage);
            bravoClient.send(bravoAnnouncementMessage);

            alphaClient.send(alphaChatMessage);
            bravoClient.send(bravoChatMessage);

            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .untilAsserted(
                            () -> {
                                assertThat(alphaMessageReceiver.getXmlMessages())
                                        .anyMatch(event ->
                                                assertThatEvent(event).hasChatMessage(bravoCallsign, bravoChatText));

                                assertThat(bravoMessageReceiver.getXmlMessages())
                                        .anyMatch(event ->
                                                assertThatEvent(event).hasChatMessage(alphaCallsign, alphaChatText));
                            });


            assertThat(alphaClient.getProtocolVersion()).isEqualTo(ProtocolVersion.XML);
            assertThat(bravoClient.getProtocolVersion()).isEqualTo(ProtocolVersion.XML);
        }
    }

    @Test
    void Connector_can_send_and_and_receive_messages_using_Protobuf_protocol() throws Exception {

        var negotiateProtocolVersion = true;
        try (var alphaClient = createConnector(negotiateProtocolVersion);
             var bravoClient = createConnector(negotiateProtocolVersion)) {

            var callsignPattern = "[{0}][{1}][Protobuf] Automated Integration Tests";
            var alphaCallsign = MessageFormat.format(
                    callsignPattern, alphaClient.getClass().getSimpleName(), "Alpha");
            var bravoCallsign = MessageFormat.format(
                    callsignPattern, bravoClient.getClass().getSimpleName(), "Bravo");

            var alphaChatText = MessageFormat.format("Test message {0}", UUID.randomUUID());
            var bravoChatText = MessageFormat.format("Test message {0}", UUID.randomUUID());

            var alphaAnnouncementMessage = ClientProtobufMessage.announcement(alphaUid, alphaCallsign);
            var alphaChatMessage = ClientProtobufMessage.chat(alphaUid, alphaCallsign, alphaChatText);

            var bravoAnnouncementMessage = ClientProtobufMessage.announcement(bravoUid, bravoCallsign);
            var bravoChatMessage = ClientProtobufMessage.chat(bravoUid, bravoCallsign, bravoChatText);

            var alphaMessageReceiver = new Receiver();
            var bravoMessageReceiver = new Receiver();

            alphaClient.connect(alphaMessageReceiver);
            bravoClient.connect(bravoMessageReceiver);

            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .untilAsserted(
                            () -> {
                                assertThat(alphaClient.getProtocolVersion())
                                        .isEqualTo(ProtocolVersion.PROTOBUF);

                                assertThat(bravoClient.getProtocolVersion())
                                        .isEqualTo(ProtocolVersion.PROTOBUF);
                            });

            alphaClient.send(alphaAnnouncementMessage);
            bravoClient.send(bravoAnnouncementMessage);
            alphaClient.send(alphaChatMessage);
            bravoClient.send(bravoChatMessage);

            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .untilAsserted(
                            () -> {
                                assertThat(alphaMessageReceiver.getProtobufMessages())
                                        .anyMatch(message -> assertThatMessage(message)
                                                .hasChatMessage(bravoCallsign, bravoChatText));

                                assertThat(bravoMessageReceiver.getProtobufMessages())
                                        .anyMatch(message -> assertThatMessage(message)
                                                .hasChatMessage(alphaCallsign, alphaChatText));
                            });
        }
    }
}
