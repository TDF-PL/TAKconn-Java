package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Event;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class TrackAssertFluent {

    private final Event event;

    public TrackAssertFluent(Event event) {
        this.event = event;
    }

    public EventAssertFluent event() {
        return new EventAssertFluent(event);
    }

    public TrackAssertFluent hasSpeed(BigDecimal speed) {
        assertThat(event.getDetail().getTrack().getSpeed()).isEqualTo(speed);
        return this;
    }

    public TrackAssertFluent hasCourse(BigDecimal course) {
        assertThat(event.getDetail().getTrack().getCourse()).isEqualTo(course);
        return this;
    }
}
