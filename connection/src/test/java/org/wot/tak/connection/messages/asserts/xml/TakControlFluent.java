package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.ProtocolVersion;
import org.wot.tak.connection.protocol.xml.TakControl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class TakControlFluent {
    private final TakControl takControl;

    public TakControlFluent(TakControl takControl) {
        this.takControl = takControl;
    }

    public TakControlFluent hasServerProtocolSupport(ProtocolVersion protocolVersion) {
        assertThat(takControl.getTakProtocolSupport().getVersion()).isEqualTo(protocolVersion.asString());
        return this;
    }

    public TakControlFluent hasServerVersion(String serverVersion) {
        assertThat(takControl.getTakServerVersionInfo().getServerVersion()).isEqualTo(serverVersion);
        return this;
    }

    public TakControlFluent hasServerApiVersion(String apiVersion) {
        assertThat(takControl.getTakServerVersionInfo().getApiVersion()).isEqualTo(apiVersion);
        return this;
    }

    public TakControlFluent hasTakRequestVersion(ProtocolVersion protocolVersion) {
        assertThat(takControl.getTakRequest().getVersion()).isEqualTo(protocolVersion.asString());
        return this;
    }

    public TakControlFluent hasTakResponseStatus(boolean status) {
        assertThat(takControl.getTakResponse().isStatus()).isEqualTo(status);
        return this;
    }
}
