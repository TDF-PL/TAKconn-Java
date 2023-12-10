package org.wot.tak.connection.messages.asserts.protobuf;

import lombok.SneakyThrows;
import org.wot.tak.connection.protocol.protobuf.DetailMarshalling;
import org.wot.tak.connection.protocol.protobuf.Takmessage;

import java.util.Optional;

public class TakMessageAssert {

    private final Takmessage.TakMessage message;

    public TakMessageAssert(Takmessage.TakMessage message) {
        this.message = message;
    }

    public static TakMessageAssert assertThatMessage(Takmessage.TakMessage message) {
        return new TakMessageAssert(message);
    }

    @SneakyThrows
    public boolean hasChatMessage(String senderCallsign, String chatMessage) {
        var hasDetail = message.hasCotEvent() && message.getCotEvent().hasDetail();
        if (!hasDetail) {
            return false;
        }

        var xmlDetailString = message.getCotEvent().getDetail().getXmlDetail();
        var xmlDetails = DetailMarshalling.fromString(xmlDetailString);

        var senderCallSignMatched = Optional.ofNullable(xmlDetails.getChat())
                .map(chat -> senderCallsign.equals(chat.getSenderCallsign()))
                .orElse(false);

        var chatMessageMatched = Optional.ofNullable(xmlDetails.getRemarks())
                .map(remarks -> chatMessage.equals(remarks.getContent()))
                .orElse(false);

        return senderCallSignMatched && chatMessageMatched;
    }
}
