package org.wot.tak.connection.messages;

import org.wot.tak.connection.UID;
import org.wot.tak.connection.protocol.ProtocolVersion;
import org.wot.tak.connection.protocol.xml.Detail;
import org.wot.tak.connection.protocol.xml.Event;
import org.wot.tak.connection.protocol.xml.Point;
import org.wot.tak.connection.protocol.xml.TakControl;
import org.wot.tak.connection.protocol.xml.TakRequest;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class Message {

    private Message() {
    }

    public static Event takControlRequest(UID uid, ProtocolVersion protocolVersion)
            throws DatatypeConfigurationException {
        var event = new Event();
        event.setVersion(BigDecimal.valueOf(2));
        event.setUid(uid.getUid());
        event.setType("t-x-takp-q");
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
        var takProtocolRequest = new TakRequest();
        takProtocolRequest.setVersion(protocolVersion.asString());
        takControl.setTakRequest(takProtocolRequest);
        detail.setTakControl(takControl);
        event.setDetail(detail);

        return event;
    }

    static XMLGregorianCalendar now() throws DatatypeConfigurationException {
        var now = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(now);
    }

    static XMLGregorianCalendar nowPlusSeconds(int seconds) throws DatatypeConfigurationException {
        var stale = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        stale.add(GregorianCalendar.SECOND, seconds);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(stale);
    }
}
