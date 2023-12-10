package org.wot.tak.connection.protocol.protobuf;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;
import org.wot.tak.connection.protocol.xml.Detail;
import org.wot.tak.connection.protocol.xml.FlowTags;
import org.wot.tak.connection.protocol.xml.Uid;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.wot.tak.connection.messages.asserts.xml.CotAssert.assertDetail;

class DetailMarshallingTest {

    @Test
    void toString_detail_returnsStringWithOmittedXmlHeaderAndOutermostDetailTag()
            throws XMLStreamException, JAXBException {
        var detail = new Detail();
        var uid = new Uid();
        var flowTags = new FlowTags();
        detail.setUid(uid);
        detail.setFlowTags(flowTags);
        uid.setDroid("droid");
        flowTags.getOtherAttributes().put(new QName("key"), "value");

        var detailsString = DetailMarshalling.toString(detail);

        assertThat(detailsString)
                .isEqualTo("<uid Droid=\"droid\"></uid><_flow-tags_ key=\"value\"></_flow-tags_>");
    }

    @Test
    void toString_emptyDetail_returnsEmptyString() throws XMLStreamException, JAXBException {
        var detail = new Detail();

        var detailsString = DetailMarshalling.toString(detail);

        assertThat(detailsString).isEmpty();
    }

    @Test
    void toString_detailWithSetEmptyInnerElement_includeThatElement() throws XMLStreamException, JAXBException {
        var detail = new Detail();
        var uid = new Uid();
        detail.setUid(uid);

        var detailsString = DetailMarshalling.toString(detail);

        assertThat(detailsString).isEqualTo("<uid></uid>");
    }

    @Test
    void fromString_detailStringWithoutXMLHeaderAndOutermostDetailTag_returnsDetail() throws JAXBException {
        var detailString = "<uid Droid=\"droid\"></uid><_flow-tags_ key=\"value\"></_flow-tags_>";

        var detail = DetailMarshalling.fromString(detailString);

        assertDetail(detail)
                .uid()
                    .hasDroid("droid")
                .event()
                .flowTags()
                    .hasAttribute("key", "value");
    }

    @Test
    void fromString_emptyString_returnsEmptyDetail() throws JAXBException {
        var detailString = "";

        var detail = DetailMarshalling.fromString(detailString);

        assertDetail(detail)
                .uid()
                    .hasDroid("");
    }

}
