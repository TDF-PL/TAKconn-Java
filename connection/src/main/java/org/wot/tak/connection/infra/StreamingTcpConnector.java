package org.wot.tak.connection.infra;

import jakarta.xml.bind.JAXBException;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.protocol.StreamingProtocolNegotiator;
import org.wot.tak.connection.protocol.protobuf.Takmessage;
import org.wot.tak.connection.protocol.xml.Event;

import javax.xml.datatype.DatatypeConfigurationException;

public abstract class StreamingTcpConnector extends TCPConnectorBase {

    private final boolean negotiateProtobufProtocol;

    public StreamingTcpConnector(Url url, Port port, SocketFactory sFactory, boolean negotiateProtobufProtocol) {
        super(url, port, sFactory);
        this.negotiateProtobufProtocol = negotiateProtobufProtocol;
    }

    @Override
    public void connect(MessageReceiver handler) throws Exception {
        var protocolNegotiatingHandler = new ProtocolNegotiatingHandler(handler);

        if (negotiateProtobufProtocol) {
            super.connect(protocolNegotiatingHandler);
        } else {
            super.connect(handler);
        }
    }

    class ProtocolNegotiatingHandler implements MessageReceiver {

        private final StreamingProtocolNegotiator protocolNegotiator;
        private final MessageReceiver internalHandler;

        ProtocolNegotiatingHandler(MessageReceiver internalHandler) {
            this.internalHandler = internalHandler;
            this.protocolNegotiator = new StreamingProtocolNegotiator();
        }

        @Override
        public void receiveByXmlProtocol(Event event) throws DatatypeConfigurationException, JAXBException {
            var negotiationResult = protocolNegotiator.processMessage(event);

            var responseToServer = negotiationResult.getResponseToServer();

            if (responseToServer.isPresent()) {
                send(responseToServer.get());
            }

            setProtocolVersion(negotiationResult.getProtocolVersion());
            internalHandler.receiveByXmlProtocol(event);
        }

        @Override
        public void receiveByProtobufProtocol(Takmessage.TakMessage event) {
            internalHandler.receiveByProtobufProtocol(event);
        }
    }
}
