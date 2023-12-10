package org.wot.tak.connection.configuration;

import org.wot.tak.connection.protocol.ProtocolVersion;
import org.wot.tak.connection.protocol.protobuf.Takmessage;
import org.wot.tak.connection.protocol.xml.Event;

import java.io.IOException;

public interface TAKServerConnector extends AutoCloseable {

    /**
     * Retrieves the protocol version of the communication.
     * @return The {@code ProtocolVersion} representing the protocol version.
     */
    ProtocolVersion getProtocolVersion();

    /**
     * Connects to the server.
     * @param handler Handler for received XML.
     * @throws Exception If an error occurs.
     */
    void connect(MessageReceiver handler) throws Exception;

    /**
     * Sends XML to the server.
     * @param event The XML to send.
     * @throws Exception If an error occurs.
     */
    void send(Event event) throws Exception;

    void send(Takmessage.TakMessage message) throws IOException;

    /**
     * Closes the connection.
     * @throws Exception If an error occurs.
     */
    void close() throws Exception;
}
