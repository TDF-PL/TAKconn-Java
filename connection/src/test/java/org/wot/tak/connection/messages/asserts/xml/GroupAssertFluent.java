package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Event;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class GroupAssertFluent {
    private final Event event;

    public GroupAssertFluent(Event event) {
        this.event = event;
    }

    public EventAssertFluent event() {
        return new EventAssertFluent(event);
    }

    public GroupAssertFluent hasName(String name) {
        assertThat(event.getDetail().getGroup().getName()).isEqualTo(name);
        return this;
    }

    public GroupAssertFluent hasRole(String role) {
        assertThat(event.getDetail().getGroup().getRole()).isEqualTo(role);
        return this;
    }
}
