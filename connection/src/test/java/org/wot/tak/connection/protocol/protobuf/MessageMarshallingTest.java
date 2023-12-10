package org.wot.tak.connection.protocol.protobuf;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.io.PipedInputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

final class MessageMarshallingTest {

    @Test
    void toStream_TakMessage_prependMagicByte() throws IOException {
        var message = Takmessage.TakMessage.newBuilder()
                .setCotEvent(
                        Cotevent.CotEvent.newBuilder()
                                .setHow("h-g")
                ).build();

        var pipedOutput = new PipedOutputStream();
        var pipedInput = new PipedInputStream(pipedOutput);

        MessageMarshalling.toStream(message, pipedOutput);

        var magicByte = pipedInput.read();
        var actualMessage = Takmessage.TakMessage.parseDelimitedFrom(pipedInput);

        assertThat(magicByte).isEqualTo(ProtobufEventReader.MAGIC_BYTE);
        assertThat(actualMessage).isEqualTo(message);
    }
}
