package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Event;
import org.wot.tak.connection.protocol.xml.Detail;
import org.wot.tak.connection.protocol.xml.Uid;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Optional;

public final class UidAssertFluent {

    private final Event event;

    public UidAssertFluent(Event event) {
        this.event = event;
    }

    public EventAssertFluent event() {
        return new EventAssertFluent(event);
    }

    public UidAssertFluent hasDroid(String droid) {
        var actual = Optional
                .ofNullable(event.getDetail())
                .map(Detail::getUid)
                .map(Uid::getDroid)
                .orElse("");

        assertThat(actual).isEqualTo(droid);
        return this;
    }
}
