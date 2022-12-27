package wot.tak.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPConnector implements TAKServerConnector {
    public final String url;
    public final int port;

    private DatagramSocket socket;
    private InetAddress address;

    public UDPConnector(String url, String port) {
        this.url = url;
        this.port = Integer.parseInt(port);
    }

    @Override
    public void connect() throws IOException {
        socket = new DatagramSocket(this.port);
        address = InetAddress.getByName(url);
    }

    @Override
    public void sendFile(File file) throws IOException {
        StringBuilder msg = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                msg.append(line);
            }
        }
        byte[] data = msg.toString().getBytes();
        socket.send(new DatagramPacket(data, data.length, address, port));
    }

    @Override
    public void close(){
        socket.close();
    }
}
