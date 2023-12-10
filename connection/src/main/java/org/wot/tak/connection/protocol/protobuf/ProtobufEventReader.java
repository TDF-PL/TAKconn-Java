package org.wot.tak.connection.protocol.protobuf;

import java.io.IOException;
import java.io.InputStream;

public final class ProtobufEventReader {

    public static final int MAGIC_BYTE = 0xbf;

    private ProtobufEventReader() {
    }

    public static Takmessage.TakMessage readFromProtobufProtocol(InputStream inputStream) throws IOException {
        var magicByte = inputStream.read();

        if (magicByte != ProtobufEventReader.MAGIC_BYTE) {
            throw new IOException("Invalid magic byte");
        }

        return Takmessage.TakMessage.parseDelimitedFrom(inputStream);
    }
}
