package org.wot.tak.connection.protocol;

public enum ProtocolVersion {

    XML("0"),

    PROTOBUF("1");

    private final String version;

    ProtocolVersion(String version) {
        this.version = version;
    }

    public String asString() {
        return version;
    }
}
