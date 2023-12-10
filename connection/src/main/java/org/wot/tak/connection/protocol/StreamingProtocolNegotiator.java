package org.wot.tak.connection.protocol;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.wot.tak.connection.UID;
import org.wot.tak.connection.messages.Message;
import org.wot.tak.connection.protocol.xml.Event;
import org.wot.tak.connection.protocol.xml.TakControl;
import org.wot.tak.connection.protocol.xml.TakProtocolSupport;
import org.wot.tak.connection.protocol.xml.TakResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.Optional;

public class StreamingProtocolNegotiator {

    private NegotiationState negotiationState = new ProtocolNotOffered();

    public ProtocolVersion getCurrentProtocolVersion() {
        return negotiationState.getCurrentProtocolVersion();
    }

    public NegotiationResult processMessage(Event event) throws DatatypeConfigurationException {
        var processingResult = negotiationState.processMessage(event);
        negotiationState = processingResult.getNextState();
        return new NegotiationResult(negotiationState.getCurrentProtocolVersion(), processingResult.getResponse());
    }

    abstract static class NegotiationState {
        @Getter(AccessLevel.PACKAGE)
        @Setter(AccessLevel.PACKAGE)
        private ProtocolVersion currentProtocolVersion = ProtocolVersion.XML;

        abstract StateProcessingResult processMessage(Event event) throws DatatypeConfigurationException;
    }

    static class ProtocolNotOffered extends NegotiationState {

        ProtocolNotOffered() {
            this.setCurrentProtocolVersion(ProtocolVersion.XML);
        }

        @Override
        StateProcessingResult processMessage(Event event) throws DatatypeConfigurationException {

            var takControl = Optional.ofNullable(event.getDetail().getTakControl());

            var takProtocolSupport = takControl
                    .map(TakControl::getTakProtocolSupport);

            var offeredVersion = takProtocolSupport
                    .map(TakProtocolSupport::getVersion)
                    .orElse(ProtocolVersion.XML.asString());

            if (ProtocolVersion.PROTOBUF.asString().equals(offeredVersion)) {
                var uid = new UID(event.getUid());
                var takControlRequest = Message.takControlRequest(uid, ProtocolVersion.PROTOBUF);
                var nextState = new ProtocolOffered();
                return new StateProcessingResult(Optional.of(takControlRequest), nextState);
            } else {
                return new StateProcessingResult(Optional.empty(), this);
            }
        }
    }

    static class ProtocolOffered extends NegotiationState {

        ProtocolOffered() {
            this.setCurrentProtocolVersion(ProtocolVersion.XML);
        }

        @Override
        StateProcessingResult processMessage(Event event) {

            var takControl = Optional.ofNullable(event.getDetail().getTakControl());

            var takControlResponse = takControl
                    .map(TakControl::getTakResponse);

            var responseStatus = takControlResponse
                    .map(TakResponse::isStatus)
                    .orElse(false);

            if (responseStatus) {
                var nextState = new ProtocolAccepted();
                return new StateProcessingResult(Optional.empty(), nextState);
            } else {
                var nextState = new ProtocolRejected();
                return new StateProcessingResult(Optional.empty(), nextState);
            }
        }
    }

    static class ProtocolAccepted extends NegotiationState {
        ProtocolAccepted() {
            this.setCurrentProtocolVersion(ProtocolVersion.PROTOBUF);
        }

        @Override
        StateProcessingResult processMessage(Event event) {
            return new StateProcessingResult(Optional.empty(), this);
        }
    }

    static class ProtocolRejected extends NegotiationState {
        ProtocolRejected() {
            this.setCurrentProtocolVersion(ProtocolVersion.XML);
        }

        @Override
        StateProcessingResult processMessage(Event event) {
            return new StateProcessingResult(Optional.empty(), this);
        }
    }

    @Getter(AccessLevel.PUBLIC)
    @RequiredArgsConstructor
    public static class NegotiationResult {
        private final ProtocolVersion protocolVersion;
        private final Optional<Event> responseToServer;
    }

    @Getter(AccessLevel.PUBLIC)
    @RequiredArgsConstructor
    static class StateProcessingResult {
        private final Optional<Event> response;
        private final NegotiationState nextState;
    }
}

