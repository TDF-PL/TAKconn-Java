package org.wot.tak.connection.messages;

import jakarta.xml.bind.JAXBException;
import org.wot.tak.connection.UID;
import org.wot.tak.connection.protocol.protobuf.ContactOuterClass;
import org.wot.tak.connection.protocol.protobuf.Cotevent;
import org.wot.tak.connection.protocol.protobuf.DetailMarshalling;
import org.wot.tak.connection.protocol.protobuf.DetailOuterClass;
import org.wot.tak.connection.protocol.protobuf.GroupOuterClass;
import org.wot.tak.connection.protocol.protobuf.Precisionlocation;
import org.wot.tak.connection.protocol.protobuf.StatusOuterClass;
import org.wot.tak.connection.protocol.protobuf.Takmessage;
import org.wot.tak.connection.protocol.protobuf.TakvOuterClass;
import org.wot.tak.connection.protocol.protobuf.TrackOuterClass;
import org.wot.tak.connection.protocol.xml.Chat;
import org.wot.tak.connection.protocol.xml.Chatgrp;
import org.wot.tak.connection.protocol.xml.Detail;
import org.wot.tak.connection.protocol.xml.FlowTags;
import org.wot.tak.connection.protocol.xml.Link;
import org.wot.tak.connection.protocol.xml.Remarks;
import org.wot.tak.connection.protocol.xml.Uid;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

public final class ClientProtobufMessage {

    private static final double LATITUDE = 27.85009;
    private static final double LONGITUDE = -81.063847;

    private ClientProtobufMessage() {
    }

    public static Takmessage.TakMessage announcement(UID uid, String callsign)
            throws XMLStreamException, JAXBException {
        var now = System.currentTimeMillis();

        var message = Takmessage.TakMessage.newBuilder()
                .setCotEvent(
                        Cotevent.CotEvent.newBuilder()
                                .setType("a-f-G-U-C-I")
                                .setAccess("Undefined")
                                .setUid(uid.getUid())
                                .setSendTime(now)
                                .setStartTime(now)
                                .setStaleTime(now + 5 * 60 * 1000)
                                .setHow("h-g-i-g-o")
                                .setLat(LATITUDE)
                                .setLon(LONGITUDE)
                                .setHae(9999999.0)
                                .setCe(9999999.0)
                                .setLe(9999999.0)
                                .setDetail(
                                        DetailOuterClass.Detail.newBuilder()
                                                .setContact(
                                                        ContactOuterClass.Contact.newBuilder()
                                                                .setCallsign(callsign)
                                                                .setEndpoint("1:sctp")
                                                )
                                                .setGroup(
                                                        GroupOuterClass.Group.newBuilder()
                                                                .setName("Cyan")
                                                                .setRole("Team Member")
                                                )
                                                .setPrecisionLocation(
                                                        Precisionlocation.PrecisionLocation.newBuilder()
                                                                .setAltsrc("???")
                                                                .setGeopointsrc("???")
                                                )
                                                .setStatus(
                                                        StatusOuterClass.Status.newBuilder()
                                                                .setBattery(98)
                                                )
                                                .setTakv(
                                                        TakvOuterClass.Takv.newBuilder()
                                                                .setDevice("HP HP ZBook 17 G6")
                                                                .setPlatform("TAKconn-java")
                                                                .setOs("Microsoft Windows 10 Pro")
                                                                .setVersion("1.0")
                                                )
                                                .setTrack(
                                                        TrackOuterClass.Track.newBuilder()
                                                                .setCourse(0.0)
                                                                .setSpeed(0.0)
                                                )
                                                .setXmlDetail(
                                                        CreateXmlDetails(
                                                                "TAKconn-java",
                                                                "TAK-Server-" + uid,
                                                                timeToString(now)
                                                        )
                                                )
                                )
                );


        return message.build();
    }

    public static Takmessage.TakMessage chat(UID uid, String callsign, String chatMessage)
            throws DatatypeConfigurationException, XMLStreamException, JAXBException {

        var now = System.currentTimeMillis();

        var message = Takmessage.TakMessage.newBuilder()
                .setCotEvent(Cotevent.CotEvent.newBuilder()
                        .setType("b-t-f")
                        .setAccess("Undefined")
                        .setUid(uid.getUid())
                        .setSendTime(now)
                        .setStartTime(now)
                        .setStaleTime(now + 5 * 60 * 1000)
                        .setHow("h-g-i-g-o")
                        .setLat(LATITUDE)
                        .setLon(LONGITUDE)
                        .setHae(9999999.0)
                        .setCe(9999999.0)
                        .setLe(9999999.0)
                        .setDetail(DetailOuterClass.Detail.newBuilder()
                                .setXmlDetail(CreateChatXmlDetails(
                                        uid,
                                        callsign,
                                        chatMessage,
                                        "TAKconn-java",
                                        "TAK-Server-" + uid,
                                        timeToString(now)))
                        )
                );

        return message.build();
    }

    private static String CreateXmlDetails(String droid, String flowTagKey, String flowTagValue)
            throws JAXBException, XMLStreamException {
        var detail = new Detail();
        var uid = new Uid();
        detail.setUid(uid);
        uid.setDroid(droid);
        var flowTags = new FlowTags();
        detail.setFlowTags(flowTags);
        flowTags.getOtherAttributes().put(
                new QName(flowTagKey), flowTagValue);

        return DetailMarshalling.toString(detail);
    }

    private static String CreateChatXmlDetails(
            UID uid, String callsign, String chatMessage, String droid, String flowTagKey, String flowTagValue)
            throws XMLStreamException, JAXBException, DatatypeConfigurationException {
        var now = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        var xmlNow = DatatypeFactory.newInstance().newXMLGregorianCalendar(now);

        var detail = new Detail();
        var chat = new Chat();
        detail.setChat(chat);

        var allChatRooms = "All Chat Rooms";
        chat.setId(allChatRooms);
        chat.setChatroom(allChatRooms);
        chat.setSenderCallsign(callsign);
        chat.setGroupOwner(false);
        chat.setMessageId(UUID.randomUUID().toString());

        var chatgrp = new Chatgrp();
        chat.setChatgrp(chatgrp);
        chatgrp.setId(allChatRooms);
        chatgrp.setUid0(uid.getUid());
        chatgrp.setUid1(allChatRooms);

        var link = new Link();
        detail.setLink(link);
        link.setUid(uid.getUid());
        link.setType("a-f-G-U-C-I");
        link.setRelation("p-p");

        var remarks = new Remarks();
        detail.setRemarks(remarks);
        remarks.setSource(MessageFormat.format("{0}.{1}", droid, uid));
        remarks.setSourceID(uid.getUid());
        remarks.setTo(allChatRooms);
        remarks.setTime(xmlNow);
        remarks.setContent(chatMessage);

        var xmlUid = new Uid();
        detail.setUid(xmlUid);
        xmlUid.setDroid(droid);
        var flowTags = new FlowTags();
        detail.setFlowTags(flowTags);
        flowTags.getOtherAttributes().put(
                new QName(flowTagKey), flowTagValue);

        return DetailMarshalling.toString(detail);
    }

    public static String timeToString(long time) {
        var instant = Instant.ofEpochMilli(time);
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
                .withZone(ZoneId.of("UTC"));

        return formatter.format(instant);
    }
}
