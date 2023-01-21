package wot.tak.connection;

import java.io.IOException;

public class CredentialsConnector extends TCPConnector{

    private final String userName;
    private final String password;

    protected CredentialsConnector(String url, String port, String responseStoragePath, SocketFactory sFactory, String userName, String password) {
        super(url, port, responseStoragePath, sFactory);
        this.userName = userName;
        this.password = password;
    }

    public void connect() throws IOException {
        s = socketFactory.createSocket(url, port);
        super.connect();
        authenticate(userName, password);
    }

    private void authenticate(String userName, String password){
        out.println(userName);
        out.println(password);
    }
}
