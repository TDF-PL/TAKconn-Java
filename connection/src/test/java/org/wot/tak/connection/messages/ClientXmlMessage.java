package org.wot.tak.connection.messages;

import org.wot.tak.connection.UID;
import org.wot.tak.connection.protocol.xml.Chat;
import org.wot.tak.connection.protocol.xml.Chatgrp;
import org.wot.tak.connection.protocol.xml.Contact;
import org.wot.tak.connection.protocol.xml.Detail;
import org.wot.tak.connection.protocol.xml.Event;
import org.wot.tak.connection.protocol.xml.FlowTags;
import org.wot.tak.connection.protocol.xml.Group;
import org.wot.tak.connection.protocol.xml.Link;
import org.wot.tak.connection.protocol.xml.Point;
import org.wot.tak.connection.protocol.xml.Precisionlocation;
import org.wot.tak.connection.protocol.xml.Remarks;
import org.wot.tak.connection.protocol.xml.Status;
import org.wot.tak.connection.protocol.xml.Takv;
import org.wot.tak.connection.protocol.xml.Track;
import org.wot.tak.connection.protocol.xml.Uid;
import org.wot.tak.connection.protocol.xml.Usericon;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

public final class ClientXmlMessage {

    private static final double LATITUDE = 40.2100278338638;
    private static final double LONGITUDE = -92.6243997054913;
    private static final double HAE = 246.411508368538;

    private ClientXmlMessage() {
    }

    public static Event announcement(UID uid, String callsign)
            throws DatatypeConfigurationException {
        var now = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        var xmlNow = DatatypeFactory.newInstance().newXMLGregorianCalendar(now);
        var stale = xmlNow.toGregorianCalendar();
        stale.add(GregorianCalendar.MINUTE, 5);
        var xmlStale = DatatypeFactory.newInstance().newXMLGregorianCalendar(stale);

        var event = new Event();
        event.setAccess("Undefined");
        event.setHow("h-e");
        event.setStale(xmlStale);
        event.setStart(xmlNow);
        event.setTime(xmlNow);
        event.setType("a-f-G-U-C-I");
        event.setUid(uid.getUid());
        event.setVersion(BigDecimal.valueOf(2));

        var point = new Point();
        event.setPoint(point);
        point.setCe(BigDecimal.valueOf(9999999.0));
        point.setHae(BigDecimal.valueOf(HAE));
        point.setLat(BigDecimal.valueOf(LATITUDE));
        point.setLe(BigDecimal.valueOf(9999999.0));
        point.setLon(BigDecimal.valueOf(LONGITUDE));

        var detail = new Detail();
        event.setDetail(detail);

        var group = new Group();
        detail.setGroup(group);
        group.setName("Cyan");
        group.setRole("Team Member");

        var contact = new Contact();
        detail.setContact(contact);
        contact.setCallsign(callsign);
        contact.setEndpoint("*:-1:stcp");

        var status = new Status();
        detail.setStatus(status);
        status.setBattery(BigInteger.valueOf(98));

        var precisionLocation = new Precisionlocation();
        detail.setPrecisionlocation(precisionLocation);
        precisionLocation.setAltsrc("???");
        precisionLocation.setGeopointsrc("???");

        var takv = new Takv();
        detail.setTakv(takv);
        takv.setDevice("HP HP ZBook 17 G6");
        takv.setOs("Microsoft Windows 10 Pro");
        takv.setPlatform("TAKconn-java");
        takv.setVersion("1.0");

        var track = new Track();
        detail.setTrack(track);
        track.setCourse(BigDecimal.ZERO);
        track.setSpeed(BigDecimal.ZERO);

        var xmlUid = new Uid();
        detail.setUid(xmlUid);
        xmlUid.setDroid("TAKconn-java");

        var userIcon = new Usericon();
        detail.setUsericon(userIcon);
        userIcon.setIconsetpath("COT_MAPPING_2525B/a-n/a-n-G");

        return event;
    }

    public static Event chat(UID uid, String callsign, String chatMessage)
            throws DatatypeConfigurationException {
        var now = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        var xmlNow = DatatypeFactory.newInstance().newXMLGregorianCalendar(now);
        var stale = xmlNow.toGregorianCalendar();
        var xmlStale = DatatypeFactory.newInstance().newXMLGregorianCalendar(stale);
        stale.add(GregorianCalendar.MINUTE, 5);

        var chatroomName = "All Chat Rooms";
        var messageId = UUID.randomUUID().toString();
        var geoChatId = MessageFormat.format("GeoChat.{0}.{1}.{2}", uid, chatroomName, messageId);
        var remarkSource = "BAO.F.WinTAK." + uid;

        var event = new Event();
        event.setAccess("Undefined");
        event.setHow("h-g-i-g-o");
        event.setStale(xmlStale);
        event.setStart(xmlNow);
        event.setTime(xmlNow);
        event.setType("b-t-f");
        event.setUid(geoChatId);
        event.setVersion(BigDecimal.valueOf(2));

        var point = new Point();
        event.setPoint(point);
        point.setCe(BigDecimal.valueOf(9999999.0));
        point.setHae(BigDecimal.valueOf(HAE));
        point.setLat(BigDecimal.valueOf(LATITUDE));
        point.setLe(BigDecimal.valueOf(9999999.0));
        point.setLon(BigDecimal.valueOf(LONGITUDE));

        var detail = new Detail();
        event.setDetail(detail);

        var chat = new Chat();
        detail.setChat(chat);
        chat.setChatroom(chatroomName);
        chat.setGroupOwner(false);
        chat.setId(chatroomName);
        chat.setMessageId(messageId);
        chat.setSenderCallsign(callsign);

        var chatgrp = new Chatgrp();
        chat.setChatgrp(chatgrp);
        chatgrp.setId(chatroomName);
        chatgrp.setUid0(uid.getUid());
        chatgrp.setUid1(chatroomName);

        var link = new Link();
        detail.setLink(link);
        link.setRelation("p-p");
        link.setType("a-f-G-U-C-I");
        link.setUid(uid.getUid());

        var remarks = new Remarks();
        detail.setRemarks(remarks);
        remarks.setSource(remarkSource);
        remarks.setSourceID(uid.getUid());
        remarks.setTime(xmlNow);
        remarks.setTo(chatroomName);
        remarks.setContent(chatMessage);

        var flowTags = new FlowTags();
        detail.setFlowTags(flowTags);
        flowTags.getOtherAttributes().put(
                new javax.xml.namespace.QName("TAK-Server-" + UUID.randomUUID()), xmlNow.toString());

        return event;
    }
}
