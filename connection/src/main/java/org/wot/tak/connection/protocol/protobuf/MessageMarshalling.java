package org.wot.tak.connection.protocol.protobuf;

import java.io.IOException;
import java.io.OutputStream;

public final class MessageMarshalling {

    private MessageMarshalling() {
    }

    public static void toStream(Takmessage.TakMessage message, OutputStream outputStream) throws IOException {
        outputStream.write(ProtobufEventReader.MAGIC_BYTE);
        message.writeDelimitedTo(outputStream);
    }
}
