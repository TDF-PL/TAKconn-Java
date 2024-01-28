import org.junit.jupiter.api.Test;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.UID;
import org.wot.tak.connection.configuration.AuthenticationConfig;
import org.wot.tak.connection.configuration.ConnectorFactory;
import org.wot.tak.connection.configuration.ConnectorFactoryConfig;
import org.wot.tak.connection.connectors.Receiver;
import org.wot.tak.connection.protocol.ProtocolVersion;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.wot.tak.connection.messages.ClientProtobufMessage.announcement;

class ConnectorFactoryTests {

    @Test
    void Can_create_SSL_connector_using_connector_factory() throws Exception {

        var config = new ConnectorFactoryConfig(
                Url.of("tak-dev.1gs20.net"),
                Port.of(8089),
                new AuthenticationConfig(
                        "",
                        "",
                        "cert-test/truststore-int-ca.p12",
                        "atakatak",
                        "cert-test/charlie.p12",
                        "atakatak",
                        true));

        var connectorFactory = new ConnectorFactory(config);

        try (var senderConnector = connectorFactory.getSSLConnector();
             var receiverConnector = connectorFactory.getSSLConnector()) {

            senderConnector.connect(new Receiver());

            var receiver = new Receiver();
            receiverConnector.connect(receiver);

            await().atMost(5, TimeUnit.SECONDS)
                    .untilAsserted(
                            () -> {
                                assertThat(senderConnector.getProtocolVersion())
                                        .isEqualTo(ProtocolVersion.PROTOBUF);

                                assertThat(receiverConnector.getProtocolVersion())
                                        .isEqualTo(ProtocolVersion.PROTOBUF);
                            });

            var message = announcement(UID.random(), "sender");
            senderConnector.send(message);

            await().atMost(5, TimeUnit.SECONDS).untilAsserted(receiver.received(message));
        }
    }

    @Test
    void Can_create_TCP_connector_using_connector_factory() throws Exception {

        var config = new ConnectorFactoryConfig(
                Url.of("127.0.0.1"),
                Port.of(8999),
                new AuthenticationConfig(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        true));

        var connectorFactory = new ConnectorFactory(config);

        try (var senderConnector = connectorFactory.getTcpConnector();
             var receiverConnector = connectorFactory.getTcpConnector()) {

            senderConnector.connect(new Receiver());

            var receiver = new Receiver();
            receiverConnector.connect(receiver);

            await().atMost(5, TimeUnit.SECONDS)
                    .untilAsserted(
                            () -> {
                                assertThat(senderConnector.getProtocolVersion())
                                        .isEqualTo(ProtocolVersion.PROTOBUF);

                                assertThat(receiverConnector.getProtocolVersion())
                                        .isEqualTo(ProtocolVersion.PROTOBUF);
                            });

            var message = announcement(UID.random(), "sender");
            senderConnector.send(message);

            await().atMost(5, TimeUnit.SECONDS).untilAsserted(receiver.received(message));
        }
    }

    @Test
    void Can_create_UDP_connector_using_connector_factory() throws Exception {

        var config = new ConnectorFactoryConfig(
                Url.of("127.0.0.1"),
                Port.of(8999),
                new AuthenticationConfig(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        true));

        var connectorFactory = new ConnectorFactory(config);

        try (var senderConnector = connectorFactory.getUDPConnector();
             var receiverConnector = connectorFactory.getTcpConnector()) {

            senderConnector.connect(new Receiver());

            var receiver = new Receiver();
            receiverConnector.connect(receiver);

            await().atMost(5, TimeUnit.SECONDS)
                    .untilAsserted(
                            () -> {
                                assertThat(senderConnector.getProtocolVersion())
                                        .isEqualTo(ProtocolVersion.PROTOBUF);

                                assertThat(receiverConnector.getProtocolVersion())
                                        .isEqualTo(ProtocolVersion.PROTOBUF);
                            });

            var message = announcement(UID.random(), "sender");
            senderConnector.send(message);

            await().atMost(5, TimeUnit.SECONDS).untilAsserted(receiver.received(message));
        }
    }
}
