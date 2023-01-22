package org.wot.tak.connection;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PlainConnectorTest {

    @Test
    public void shouldAnswerWithTrue() {
        var connector = new PlainConnector("localhost", "1025", "/tmp", null);
        assertNotNull(connector);
    }
}
