package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Event;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class TakvAssertFluent {

    private final Event event;

    public TakvAssertFluent(Event event) {
        this.event = event;
    }

    public EventAssertFluent event() {
        return new EventAssertFluent(event);
    }

    public TakvAssertFluent hasDevice(String device) {
        assertThat(event.getDetail().getTakv().getDevice()).isEqualTo(device);
        return this;
    }

    public TakvAssertFluent hasPlatform(String platform) {
        assertThat(event.getDetail().getTakv().getPlatform()).isEqualTo(platform);
        return this;
    }

    public TakvAssertFluent hasOs(String os) {
        assertThat(event.getDetail().getTakv().getOs()).isEqualTo(os);
        return this;
    }

    public TakvAssertFluent hasVersion(String version) {
        assertThat(event.getDetail().getTakv().getVersion()).isEqualTo(version);
        return this;
    }
}
