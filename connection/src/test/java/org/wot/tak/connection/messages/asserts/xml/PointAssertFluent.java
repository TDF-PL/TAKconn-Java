package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Event;
import org.wot.tak.connection.protocol.xml.Point;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class PointAssertFluent {
    private final Point point;
    private final Event event;

    public PointAssertFluent(Event event) {
        this.event = event;
        this.point = event.getPoint();
    }

    public EventAssertFluent event() {
        return new EventAssertFluent(event);
    }

    public PointAssertFluent hasLat(BigDecimal lat) {
        assertThat(point.getLat()).isEqualTo(lat);
        return this;
    }

    public PointAssertFluent hasLon(BigDecimal lon) {
        assertThat(point.getLon()).isEqualTo(lon);
        return this;
    }

    public PointAssertFluent hasHae(BigDecimal hae) {
        assertThat(point.getHae()).isEqualTo(hae);
        return this;
    }

    public PointAssertFluent hasCe(BigDecimal ce) {
        assertThat(point.getCe()).isEqualTo(ce);
        return this;
    }

    public PointAssertFluent hasLe(BigDecimal le) {
        assertThat(point.getLe()).isEqualTo(le);
        return this;
    }
}
