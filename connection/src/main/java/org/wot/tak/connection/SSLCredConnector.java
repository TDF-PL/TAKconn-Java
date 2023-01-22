package org.wot.tak.connection;

import java.io.IOException;

public class SSLCredConnector extends SSLConnector {

    private final String userName;
    private final String password;

    protected SSLCredConnector(String url, String port, String responseStoragePath, SocketFactory sFactory, String userName, String password) {
        super(url, port, responseStoragePath, sFactory);
        this.userName = userName;
        this.password = password;
    }

    public void connect() throws IOException {
        super.connect();
        authenticate(userName, password);
    }

    private void authenticate(String userName, String password){
        // TODO Implement sending cot with credentials
    }
}
