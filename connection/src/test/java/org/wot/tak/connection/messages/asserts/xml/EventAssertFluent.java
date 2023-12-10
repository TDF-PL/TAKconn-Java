package org.wot.tak.connection.messages.asserts.xml;

import lombok.SneakyThrows;
import org.wot.tak.connection.protocol.xml.Detail;
import org.wot.tak.connection.protocol.xml.Event;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class EventAssertFluent {

    private final Event event;

    public static EventAssertFluent assertThatEvent(Event event) {
        return new EventAssertFluent(event);
    }

    public EventAssertFluent(Event event) {

        this.event = event;
    }

    public TakControlFluent takControl() {
        return new TakControlFluent(event.getDetail().getTakControl());
    }


    public PointAssertFluent point() {
        return new PointAssertFluent(event);
    }

    public GroupAssertFluent group() {
        return new GroupAssertFluent(event);
    }

    public ContactAssertFluent contact() {
        return new ContactAssertFluent(event);
    }

    public StatusAssertFluent status() {
        return new StatusAssertFluent(event);
    }

    public TrackAssertFluent track() {
        return new TrackAssertFluent(event);
    }

    public TakvAssertFluent takv() {
        return new TakvAssertFluent(event);
    }

    public UidAssertFluent uid() {
        return new UidAssertFluent(event);
    }

    public FlowTagsAssertFluent flowTags() {
        return new FlowTagsAssertFluent(event);
    }

    public EventAssertFluent hasVersion(BigDecimal version) {
        assertThat(event.getVersion()).isEqualTo(version);
        return this;
    }

    public EventAssertFluent hasUid(String uid) {
        assertThat(event.getUid()).isEqualTo(uid);
        return this;
    }

    public EventAssertFluent hasType(String type) {
        assertThat(event.getType()).isEqualTo(type);
        return this;
    }

    public EventAssertFluent hasHow(String how) {
        assertThat(event.getHow()).isEqualTo(how);
        return this;
    }

    public EventAssertFluent hasTime(String time) {
        assertThat(event.getTime().toString()).isEqualTo(time);
        return this;
    }

    public EventAssertFluent hasStart(String start) {
        assertThat(event.getStart().toString()).isEqualTo(start);
        return this;
    }

    public EventAssertFluent hasStale(String stale) {
        assertThat(event.getStale().toString()).isEqualTo(stale);
        return this;
    }

    public EventAssertFluent hasAccess(String access) {
        assertThat(event.getAccess()).isEqualTo(access);
        return this;
    }

    @SneakyThrows
    public boolean hasChatMessage(String senderCallsign, String chatMessage) {
        var detail = Optional.ofNullable(event.getDetail());

        var senderCallSignMatched = detail
                .map(Detail::getChat)
                .map(chat -> senderCallsign.equals(chat.getSenderCallsign()))
                .orElse(false);

        var chatMessageMatched = detail
                .map(Detail::getRemarks)
                .map(remarks -> chatMessage.equals(remarks.getContent()))
                .orElse(false);

        return senderCallSignMatched && chatMessageMatched;
    }
}
