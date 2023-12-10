package org.wot.tak.connection.infra;

import jakarta.xml.bind.JAXBException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.configuration.TAKServerConnector;
import org.wot.tak.connection.protocol.protobuf.MessageMarshalling;
import org.wot.tak.connection.protocol.protobuf.Takmessage;
import org.wot.tak.connection.protocol.xml.EventMarshalling;
import org.wot.tak.connection.protocol.protobuf.ProtobufEventReader;
import org.wot.tak.connection.protocol.ProtocolVersion;
import org.wot.tak.connection.protocol.xml.XmlEventReader;
import org.wot.tak.connection.protocol.xml.Event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Getter(AccessLevel.PUBLIC)
public abstract class TCPConnectorBase implements TAKServerConnector {
    private final Url url;
    private final Port port;
    private final SocketFactory socketFactory;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ResponseListener listener;
    private MessageReceiver handler;

    @Setter(AccessLevel.PUBLIC)
    private Socket socket;

    @Setter(AccessLevel.PUBLIC)
    private ProtocolVersion protocolVersion = ProtocolVersion.XML;

    protected TCPConnectorBase(Url url, Port port, SocketFactory sFactory) {
        this.url = url;
        this.port = port;
        this.socketFactory = sFactory;
    }

    @Override
    public void send(Event xml) throws JAXBException {
        EventMarshalling.toStream(xml, outputStream);
    }

    @Override
    public void send(Takmessage.TakMessage message) throws IOException {
        MessageMarshalling.toStream(message, outputStream);
    }

    @Override
    public void connect(MessageReceiver handler) throws Exception {
        this.handler = handler;
        this.initializeInOut();
        listener = new ResponseListener();
        listener.start();
    }

    private void initializeInOut() throws IOException {
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
    }

    private final class ResponseListener extends Thread {

        private volatile boolean stop = false;

        void finish() {
            stop = true;
        }

        public void run() {
            while (!stop) {
                try {
                    if (protocolVersion == ProtocolVersion.XML) {
                        var event = XmlEventReader.readFromXmlProtocol(inputStream);
                        handler.receiveByXmlProtocol(event);
                    } else if (protocolVersion == ProtocolVersion.PROTOBUF) {
                        var event = ProtobufEventReader.readFromProtobufProtocol(inputStream);
                        handler.receiveByProtobufProtocol(event);
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    public void close() throws Exception {
        listener.finish();
        Thread.sleep(200); //todo: remove this sleep and do more graceful shutdown
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
