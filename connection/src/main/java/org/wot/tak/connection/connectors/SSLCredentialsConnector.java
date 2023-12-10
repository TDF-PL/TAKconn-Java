package org.wot.tak.connection.connectors;

import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.infra.SocketFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public final class SSLCredentialsConnector extends SSLConnector {

    private final String username;
    private final String password;

    public SSLCredentialsConnector(String url, String port, SocketFactory sFactory, String username, String password) {
        super(url, port, sFactory);
        this.username = username;
        this.password = password;
    }

    @Override
    public void connect(MessageReceiver handler) throws Exception {
        super.connect(handler);
        this.authenticate();
    }

    private void authenticate() throws Exception {
        var docFactory = DocumentBuilderFactory.newInstance();
        var docBuilder = docFactory.newDocumentBuilder();
        var doc = docBuilder.newDocument();
        var auth = doc.createElement("auth");
        doc.appendChild(auth);
        var cot = doc.createElement("cot");
        cot.setAttribute("username", username);
        cot.setAttribute("password", password);
        //cot.setAttribute("uid", uid);
        auth.appendChild(cot);
        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();
        var source = new DOMSource(doc);
        var output = new StringWriter();
        var result = new StreamResult(output);
        transformer.transform(source, result);
//        super.send(getPrintWriter().toString());
    }
}
