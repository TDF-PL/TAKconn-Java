package wot.tak.connection;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class MessageValidator {

    private final Schema schema;

    public MessageValidator(String xsdPath) throws IOException, SAXException{
        if (xsdPath.isEmpty()){
            throw new IOException("Path to xsd is empty");
        }
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = factory.newSchema(new File(xsdPath));
    }

    public void validateXMLSchema(String xmlPath) throws IOException, SAXException{
        if (xmlPath.isEmpty()){
            throw new IOException("Path to xml is empty");
        }
        Validator validator = this.schema.newValidator();
        validator.validate(new StreamSource(new File(xmlPath)));
    }

}