package org.wot.tak.connection.protocol.xml;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;
import org.wot.tak.connection.protocol.ProtocolVersion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.wot.tak.connection.messages.asserts.xml.CotAssert.assertEvent;

public class XmBytesUnmarshallingTest {

    @Test
    void Unmarshalling_of_WinTak_announcement() throws JAXBException {
        var announcement = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<event version='2.0' uid='S-1-5-21-2748325954-2080330476-2770939353-1516' "
                    + "type='a-f-G-U-C-I' how='h-g-i-g-o' time='2023-10-19T08:41:47Z' "
                    + "start='2023-10-19T08:41:47Z' stale='2023-10-19T08:48:02Z' access='Undefined'>"
                + "<point lat='0.0' lon='0.0' hae='0.0' ce='9999999.0' le='9999999.0'/>"
                + "<detail>"
                    + "<contact callsign='BRIGG' endpoint='*:-1:stcp'/>"
                    + "<__group name='Cyan' role='Team Member'/>"
                    + "<status battery='98'/>"
                    + "<takv device='HP HP ZBook 17 G6' platform='WinTAK-CIV' os='Microsoft Windows 10 Pro' "
                        + "version='4.10.0.162'/>"
                    + "<track speed='0.0' course='0.0'/>"
                    + "<uid Droid='BRIGG'/>"
                + "</detail>"
                + "</event>";

        var eventBack = EventMarshalling.fromBytes(announcement.getBytes());

        assertEvent(eventBack)
                    .hasVersion(BigDecimal.valueOf(2.0))
                    .hasUid("S-1-5-21-2748325954-2080330476-2770939353-1516")
                    .hasType("a-f-G-U-C-I")
                    .hasHow("h-g-i-g-o")
                    .hasTime("2023-10-19T08:41:47Z")
                    .hasStart("2023-10-19T08:41:47Z")
                    .hasStale("2023-10-19T08:48:02Z")
                    .hasAccess("Undefined")
                .point()
                    .hasLat(BigDecimal.valueOf(0.0))
                    .hasLon(BigDecimal.valueOf(0.0))
                    .hasHae(BigDecimal.valueOf(0.0))
                    .hasCe(BigDecimal.valueOf(9999999.0))
                    .hasLe(BigDecimal.valueOf(9999999.0))
                .event().contact()
                    .hasCallsign("BRIGG")
                    .hasEndpoint("*:-1:stcp")
                .event().group()
                    .hasName("Cyan")
                    .hasRole("Team Member")
                .event().status()
                    .hasBattery(BigInteger.valueOf(98))
                .event().takv()
                    .hasDevice("HP HP ZBook 17 G6")
                    .hasPlatform("WinTAK-CIV")
                    .hasOs("Microsoft Windows 10 Pro")
                    .hasVersion("4.10.0.162")
                .event().track()
                    .hasSpeed(BigDecimal.valueOf(0.0))
                    .hasCourse(BigDecimal.valueOf(0.0))
                .event().uid()
                    .hasDroid("BRIGG");
    }

    @Test
    void Unmarshalling_of_TakProtocolSupport_announcement() throws JAXBException {
        var announcement = "<?xml version='1.0' encoding='utf-16'?>"
                         + "<event version='2.0' uid='78de4a78-3b79-4433-a37e-f433b0fb2f6a' type='t-x-takp-v' "
                            + "time='2023-10-19T09:06:20Z' start='2023-10-19T09:06:20Z' "
                            + "stale='2023-10-19T09:07:20Z' how='m-g'>"
                         + "<point lat='0.0' lon='0.0' hae='0.0' ce='999999' le='999999' />"
                         + "<detail>"
                            + "<TakControl>"
                                + "<TakProtocolSupport version='1' />"
                                + "<TakServerVersionInfo serverVersion='4.8-RELEASE-31-HEAD' apiVersion='3' />"
                            + "</TakControl>"
                         + "</detail>"
                         + "</event>";

        var eventBack = EventMarshalling.fromBytes(announcement.getBytes(StandardCharsets.UTF_16));

        assertEvent(eventBack)
                    .hasVersion(BigDecimal.valueOf(2.0))
                    .hasUid("78de4a78-3b79-4433-a37e-f433b0fb2f6a")
                    .hasType("t-x-takp-v")
                    .hasHow("m-g")
                    .hasTime("2023-10-19T09:06:20Z")
                    .hasStart("2023-10-19T09:06:20Z")
                    .hasStale("2023-10-19T09:07:20Z")
                .point()
                    .hasLat(BigDecimal.valueOf(0.0))
                    .hasLon(BigDecimal.valueOf(0.0))
                    .hasHae(BigDecimal.valueOf(0.0))
                    .hasCe(BigDecimal.valueOf(999999))
                    .hasLe(BigDecimal.valueOf(999999))
                .event()
                .takControl()
                    .hasServerProtocolSupport(ProtocolVersion.PROTOBUF)
                    .hasServerVersion("4.8-RELEASE-31-HEAD")
                    .hasServerApiVersion("3");
    }

    @Test
    void Unmarshalling_of_TakProtocolResponse() throws JAXBException {
        var response = "<?xml version='1.0' encoding='utf-16'?>"
                         + "<event version='2.0' uid='78de4a78-3b79-4433-a37e-f433b0fb2f6a' type='t-x-takp-r' "
                                + "time='2023-10-19T09:06:20Z' start='2023-10-19T09:06:20Z' "
                                + "stale='2023-10-19T09:07:20Z' how='m-g'>"
                         + "<point lat='0.0' lon='0.0' hae='0.0' ce='999999' le='999999'/>"
                         + "<detail>"
                            + "<TakControl>"
                                + "<TakResponse status='true'/>"
                            + "</TakControl>"
                         + "</detail>"
                         + "</event>";

        var eventBack = EventMarshalling.fromBytes(response.getBytes(StandardCharsets.UTF_16));

        assertEvent(eventBack)
                    .hasVersion(BigDecimal.valueOf(2.0))
                    .hasUid("78de4a78-3b79-4433-a37e-f433b0fb2f6a")
                    .hasType("t-x-takp-r")
                    .hasHow("m-g")
                    .hasTime("2023-10-19T09:06:20Z")
                    .hasStart("2023-10-19T09:06:20Z")
                    .hasStale("2023-10-19T09:07:20Z")
                .point()
                    .hasLat(BigDecimal.valueOf(0.0))
                    .hasLon(BigDecimal.valueOf(0.0))
                    .hasHae(BigDecimal.valueOf(0.0))
                    .hasCe(BigDecimal.valueOf(999999))
                    .hasLe(BigDecimal.valueOf(999999))
                .event()
                .takControl()
                    .hasTakResponseStatus(true);
    }
}
