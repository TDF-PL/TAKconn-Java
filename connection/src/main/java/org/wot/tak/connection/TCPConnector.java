package org.wot.tak.connection;

import javax.sql.rowset.spi.XmlReader;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;

public abstract class TCPConnector implements TAKServerConnector {
    public final String url;
    public final int port;
    private final String responseStoragePath;
    protected SocketFactory socketFactory;
    protected Socket s;
    protected InputStream in;
    protected PrintWriter out;
    private ResponseListener listener;

    protected TCPConnector(String url, String port, String responseStoragePath, SocketFactory sFactory) {
        this.url = url;
        this.port = Integer.parseInt(port);
        this.responseStoragePath = responseStoragePath;
        this.socketFactory = sFactory;
    }

    public void sendFile(File file) throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.println(line);
            }
        }
    }

    public void connect() throws IOException {
        this.initializeInOut();
        s.setSoTimeout(100);
        listener = new ResponseListener();
        listener.start();
    }

    private void initializeInOut() throws IOException {
        out = new PrintWriter(s.getOutputStream(), true);
        in = s.getInputStream();
    }

    private class ResponseListener extends Thread {

        private volatile boolean stop = false;

        protected void finish(){
            stop = true;
        }
        public void run(){
            String xml;
            File file;
            PrintWriter pw;
            while (!stop) {
                try {
                    xml = EventReader.readFrom(in);
                    file = new File(responseStoragePath + "/" + System.currentTimeMillis() + ".cot");
                    Files.createDirectories(file.getParentFile().toPath());
                    pw = new PrintWriter(new FileWriter(file));
                    pw.println(xml);
                    pw.close();
                } catch (SocketTimeoutException ex) {
                    continue;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    public void close() throws Exception {
        listener.finish();
        Thread.sleep(200);
        out.close();
        in.close();
        s.close();
    }
}
