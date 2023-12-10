package org.wot.tak.connection.protocol;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wot.tak.connection.UID;
import org.wot.tak.connection.messages.ServerMessage;
import org.wot.tak.connection.protocol.xml.TakControl;
import org.wot.tak.connection.protocol.xml.TakRequest;

import javax.xml.datatype.DatatypeConfigurationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StreamingProtocolNegotiatorTest {

    @Test
    void ctor_newInstance_indicatesXMLProtocol() {
        var negotiator = new StreamingProtocolNegotiator();
        Assertions.assertEquals(ProtocolVersion.XML, negotiator.getCurrentProtocolVersion());
    }

    @Test
    void processMessage_TakProtocolSupportReceived_createsTakControlRequestToAcceptOfferedProtocol()
            throws DatatypeConfigurationException {

        var negotiator = new StreamingProtocolNegotiator();

        var serverUid = UID.random();
        var takProtocolSupport = ServerMessage.takProtocolSupport(serverUid, ProtocolVersion.PROTOBUF);

        var negotiationResult = negotiator.processMessage(takProtocolSupport);

        assertThat(negotiationResult.getResponseToServer())
                .map(r -> r.getDetail().getTakControl())
                .map(TakControl::getTakRequest)
                .map(TakRequest::getVersion)
                .hasValue(ProtocolVersion.PROTOBUF.asString());
    }

    @Test
    void processMessage_TakProtocolSupportReceived_stillXMLProtocolInUse()
            throws DatatypeConfigurationException {

        var negotiator = new StreamingProtocolNegotiator();
        var serverUid = UID.random();
        var takProtocolSupport = ServerMessage.takProtocolSupport(serverUid, ProtocolVersion.PROTOBUF);

        var negotiationResult = negotiator.processMessage(takProtocolSupport);

        assertThat(negotiator.getCurrentProtocolVersion()).isEqualTo(ProtocolVersion.XML);
        assertThat(negotiationResult.getProtocolVersion()).isEqualTo(ProtocolVersion.XML);
    }

    @Test
    void processMessage_TakProtocolSupportReceived_TakControlResponseWithAcceptanceReceiver_switchToProtobuf()
            throws DatatypeConfigurationException {

        var negotiator = new StreamingProtocolNegotiator();
        var serverUid = UID.random();

        var takProtocolSupport = ServerMessage.takProtocolSupport(serverUid, ProtocolVersion.PROTOBUF);
        var takControlResponse = ServerMessage.takControlResponse(serverUid, true);

        negotiator.processMessage(takProtocolSupport);
        var negotiationResult = negotiator.processMessage(takControlResponse);

        assertThat(negotiator.getCurrentProtocolVersion()).isEqualTo(ProtocolVersion.PROTOBUF);
        assertThat(negotiationResult.getProtocolVersion()).isEqualTo(ProtocolVersion.PROTOBUF);
    }
}
