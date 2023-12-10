package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Event;

import javax.xml.namespace.QName;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FlowTagsAssertFluent {

    private final Event event;

    public FlowTagsAssertFluent(Event event) {

        this.event = event;
    }

    public EventAssertFluent event() {
        return new EventAssertFluent(event);
    }

    public FlowTagsAssertFluent hasAttribute(String key, String value) {
        var flowtag = event.getDetail().getFlowTags();
        assertThat(flowtag.getOtherAttributes()).containsEntry(new QName(key), value);
        return this;
    }
}
