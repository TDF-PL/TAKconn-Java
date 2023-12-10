package org.wot.tak.connection.protocol.xml;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;
import org.wot.tak.connection.UID;
import org.wot.tak.connection.messages.Message;
import org.wot.tak.connection.messages.ServerMessage;
import org.wot.tak.connection.protocol.ProtocolVersion;

import javax.xml.datatype.DatatypeConfigurationException;

import static org.wot.tak.connection.messages.asserts.xml.CotAssert.assertEvent;

public class JAXBPojoMarshallingUnmarshallingTest {

    @Test
    void Marshaling_and_unmarshalling_of_TakProtocolSupport_message()
            throws DatatypeConfigurationException, JAXBException {

        var uid = UID.random();
        var protocol = ProtocolVersion.PROTOBUF;
        var event = ServerMessage.takProtocolSupport(uid, protocol);

        var bytes = EventMarshalling.toBytes(event);
        var eventBack = EventMarshalling.fromBytes(bytes);

        assertEvent(eventBack)
                    .hasUid(uid.toString())
                .takControl()
                    .hasServerProtocolSupport(protocol);
    }

    @Test
    void Marshaling_and_unmarshalling_of_TakRequest_message()
            throws DatatypeConfigurationException, JAXBException {

        var uid = UID.random();
        var protocol = ProtocolVersion.PROTOBUF;
        var event = Message.takControlRequest(uid, protocol);

        var bytes = EventMarshalling.toBytes(event);
        var eventBack = EventMarshalling.fromBytes(bytes);

        assertEvent(eventBack)
                    .hasUid(uid.toString())
                .takControl()
                    .hasTakRequestVersion(protocol);
    }

    @Test
    void Marshaling_and_unmarshalling_of_TakResponse_message()
            throws DatatypeConfigurationException, JAXBException {

        var uid = UID.random();
        var status = true;
        var event = ServerMessage.takControlResponse(uid, status);

        var bytes = EventMarshalling.toBytes(event);
        var eventBack = EventMarshalling.fromBytes(bytes);

        assertEvent(eventBack)
                    .hasUid(uid.toString())
                .takControl()
                    .hasTakResponseStatus(status);
    }
}
