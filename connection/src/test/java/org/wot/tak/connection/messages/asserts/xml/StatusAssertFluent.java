package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Event;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class StatusAssertFluent {
    private final Event event;

    public StatusAssertFluent(Event event) {
        this.event = event;
    }

    public EventAssertFluent event() {
        return new EventAssertFluent(event);
    }

    public StatusAssertFluent hasBattery(BigInteger status) {
        assertThat(event.getDetail().getStatus().getBattery()).isEqualTo(status);
        return this;
    }

    public StatusAssertFluent hasReadiness(Boolean readiness) {
        assertThat(event.getDetail().getStatus().isReadiness()).isEqualTo(readiness);
        return this;
    }

}
