package org.wot.tak.connection.messages;

import org.wot.tak.connection.UID;
import org.wot.tak.connection.protocol.ProtocolVersion;
import org.wot.tak.connection.protocol.xml.Detail;
import org.wot.tak.connection.protocol.xml.Event;
import org.wot.tak.connection.protocol.xml.Point;
import org.wot.tak.connection.protocol.xml.TakControl;
import org.wot.tak.connection.protocol.xml.TakProtocolSupport;
import org.wot.tak.connection.protocol.xml.TakResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;

import static org.wot.tak.connection.messages.Message.now;
import static org.wot.tak.connection.messages.Message.nowPlusSeconds;

public final class ServerMessage {

    private ServerMessage() {
    }

    public static Event takProtocolSupport(UID serverUid, ProtocolVersion protocolVersion)
            throws DatatypeConfigurationException {
        var event = new Event();
        event.setVersion(BigDecimal.valueOf(2));
        event.setUid(serverUid.getUid());
        event.setType("t-x-takp-v");
        event.setTime(now());
        event.setStart(now());
        event.setStale(nowPlusSeconds(60));
        event.setHow("m-g");

        var point = new Point();
        point.setLat(BigDecimal.ZERO);
        point.setLon(BigDecimal.ZERO);
        point.setHae(BigDecimal.ZERO);
        point.setCe(BigDecimal.valueOf(999999));
        point.setLe(BigDecimal.valueOf(999999));
        event.setPoint(point);

        var detail = new Detail();
        var takControl = new TakControl();
        var takProtocolSupport = new TakProtocolSupport();
        takProtocolSupport.setVersion(protocolVersion.asString());
        takControl.setTakProtocolSupport(takProtocolSupport);
        detail.setTakControl(takControl);
        event.setDetail(detail);

        return event;
    }

    public static Event takControlResponse(UID serverUid, boolean responseStatus)
            throws DatatypeConfigurationException {
        var event = new Event();
        event.setVersion(BigDecimal.valueOf(2));
        event.setUid(serverUid.getUid());
        event.setType("t-x-takp-r");
        event.setTime(now());
        event.setStart(now());
        event.setStale(nowPlusSeconds(60));
        event.setHow("m-g");

        var point = new Point();
        point.setLat(BigDecimal.ZERO);
        point.setLon(BigDecimal.ZERO);
        point.setHae(BigDecimal.ZERO);
        point.setCe(BigDecimal.valueOf(999999));
        point.setLe(BigDecimal.valueOf(999999));
        event.setPoint(point);

        var detail = new Detail();
        var takControl = new TakControl();
        var takControlResponse = new TakResponse();
        takControlResponse.setStatus(responseStatus);
        takControl.setTakResponse(takControlResponse);
        detail.setTakControl(takControl);
        event.setDetail(detail);

        return event;
    }
}
