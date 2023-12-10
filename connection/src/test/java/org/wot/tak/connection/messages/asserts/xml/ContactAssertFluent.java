package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Event;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class ContactAssertFluent {
    private final Event event;

    public ContactAssertFluent(Event event) {
        this.event = event;
    }

    public EventAssertFluent event() {
        return new EventAssertFluent(event);
    }

    public ContactAssertFluent hasCallsign(String callsign) {
        assertThat(event.getDetail().getContact().getCallsign()).isEqualTo(callsign);
        return this;
    }

    public ContactAssertFluent hasEndpoint(String endpoint) {
        assertThat(event.getDetail().getContact().getEndpoint()).isEqualTo(endpoint);
        return this;
    }
}
