package org.wot.tak.connection.connectors;

import com.google.protobuf.CodedOutputStream;
import jakarta.xml.bind.JAXBException;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.configuration.TAKServerConnector;
import org.wot.tak.connection.protocol.ProtocolVersion;
import org.wot.tak.connection.protocol.protobuf.Takmessage;
import org.wot.tak.connection.protocol.xml.Event;
import org.wot.tak.connection.protocol.xml.EventMarshalling;
import org.wot.tak.connection.protocol.xml.XmlEventReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public final class UDPConnector implements TAKServerConnector {
    private final Url url;
    private final Port port;
    private DatagramSocket socket;
    private InetAddress address;
    private MessageReceiver handler;
    private ResponseListener listener;
    private final ProtocolVersion protocolVersion = ProtocolVersion.PROTOBUF;
    private static final int RECEIVING_BUFFER_SIZE = 3 * 1024;
    public static final int MAGIC_BYTE = 0xbf;

    public UDPConnector(Url url, Port port) {
        this.url = url;
        this.port = port;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public void connect(MessageReceiver handler) throws Exception {
        this.socket = new DatagramSocket();
        this.address = InetAddress.getByName(url.getUrl());
        this.handler = handler;
        this.listener = new ResponseListener();
        this.listener.start();
    }

    @Override
    public void send(Event event) throws IOException, JAXBException {
        var bytes = EventMarshalling.toBytes(event);
        socket.send(new DatagramPacket(bytes, bytes.length, address, port.getNumber()));
    }

    @Override
    public void send(Takmessage.TakMessage message) throws IOException {
        var bytes = asBytesWithHeader(message.toByteArray());
        socket.send(new DatagramPacket(bytes, bytes.length, address, port.getNumber()));
    }

    @Override
    public void close() {
        listener.finish();
        socket.close();
    }

    public static byte[] asBytesWithHeader(byte[] message) throws IOException {
        var protocolVersion = Integer.parseInt(ProtocolVersion.PROTOBUF.asString());

        var varintSize = CodedOutputStream.computeUInt32SizeNoTag(protocolVersion);
        var outputStream = new ByteArrayOutputStream(1 + varintSize + 1 + protocolVersion);

        outputStream.write(MAGIC_BYTE);

        var codedOutputStream = CodedOutputStream.newInstance(outputStream);
        codedOutputStream.writeUInt32NoTag(protocolVersion);
        codedOutputStream.flush();

        outputStream.write(MAGIC_BYTE);

        outputStream.write(message);
        return outputStream.toByteArray();
    }

    private final class ResponseListener extends Thread {

        private volatile boolean stop = false;

        void finish() {
            stop = true;
        }

        public void run() {
            while (!stop) {
                try {
                    var buffer = new byte[RECEIVING_BUFFER_SIZE];
                    var packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    var data = packet.getData();
                    var event = XmlEventReader.readFromXmlProtocol(data);
                    handler.receiveByXmlProtocol(event);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}
