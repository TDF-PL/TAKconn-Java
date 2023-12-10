package org.wot.tak.connection.connectors;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.UID;
import org.wot.tak.connection.messages.ClientProtobufMessage;
import org.wot.tak.connection.messages.ClientXmlMessage;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wot.tak.connection.messages.asserts.protobuf.TakMessageAssert.assertThatMessage;
import static org.wot.tak.connection.messages.asserts.xml.EventAssertFluent.assertThatEvent;

class UDPConnectorTest {

    private final Url serverUrl = new Url("127.0.0.1");

    private final Port port = Port.of(18999);

    UDPConnectorTest() {
    }

    @Test
    void Connector_can_send_and_and_receive_messages_using_XML_protocol() throws Exception {
        var alphaUid = new UID("68518549-dfba-4089-9452-94f2523c10e7");
        var bravoUid = new UID("c4708e58-8f1a-4e92-aa4c-c7038983dbaf");

        try (var alphaClient = new UDPConnector(serverUrl, port);
             var bravoClient = new UDPConnector(serverUrl, port)) {

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
        }
    }

    @Test
    void Connector_can_send_and_and_receive_messages_using_Protobuf_protocol() throws Exception {
        var alphaUid = new UID("38a874d5-ed72-40c8-b968-b9a6f4286e21");
        var bravoUid = new UID("98f537b1-448a-4665-a58d-95ddd6c2bc9c");

        try (var alphaClient = new UDPConnector(serverUrl, port);
             var bravoClient = new UDPConnector(serverUrl, port)) {

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
