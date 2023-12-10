package org.wot.tak.connection.connectors;

import org.wot.tak.common.Port;
import org.wot.tak.connection.configuration.MessageReceiver;
import org.wot.tak.connection.infra.SocketFactory;
import org.wot.tak.connection.infra.TCPConnectorBase;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public final class CredentialsConnector extends TCPConnectorBase {

    private final String username;
    private final String password;

    public CredentialsConnector(String url, Port port, SocketFactory sFactory, String username, String password) {
        super(url, port, sFactory);
        this.username = username;
        this.password = password;
    }

    @Override
    public void connect(MessageReceiver handler) throws Exception {
        this.setSocket(getSocketFactory().createSocket(getUrl(), getPort()));
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
