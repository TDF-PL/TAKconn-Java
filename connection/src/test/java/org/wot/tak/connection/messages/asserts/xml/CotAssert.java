package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Auth;
import org.wot.tak.connection.protocol.xml.Detail;
import org.wot.tak.connection.protocol.xml.Event;

public final class CotAssert {

    private CotAssert() {
    }

    public static EventAssertFluent assertEvent(Event event) {
        return new EventAssertFluent(event);
    }
    public static AuthAssertFluent assertAuth(Auth auth) {
        return new AuthAssertFluent(auth);
    }

    public static EventAssertFluent assertDetail(Detail detail) {
        var event = new Event();
        event.setDetail(detail);
        return new EventAssertFluent(event);
    }
}


