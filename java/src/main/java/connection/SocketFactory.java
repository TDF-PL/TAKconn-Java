package main.java.connection;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class SocketFactory {
    private final String keyStoreFilepath;
    private final String keyStorePassword;
    private final String trustStoreFilepath;
    private final String trustStorePassword;
    private SSLSocketFactory sslFactory;

    protected SocketFactory(Properties prop) throws Exception{
        keyStoreFilepath = prop.getProperty("key.store.path");
        keyStorePassword = prop.getProperty("key.store.password");
        trustStoreFilepath = prop.getProperty("trust.store.path");
        trustStorePassword = prop.getProperty("trust.store.password");
        if (Boolean.parseBoolean(prop.getProperty("server.certificate.verification"))) {
            initializeSocketFactory();
        } else {
            initializeTestSocketFactory();
        }
    }

    protected Socket createSSLSocket(String url, int port) throws IOException {
        return sslFactory.createSocket(url, port);
    }

    protected Socket createSocket(String url, int port) throws IOException{
        return new Socket(url, port);
    }

    private void initializeTestSocketFactory() throws Exception{
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        KeyManagerFactory kmf;
        KeyStore ks;
        char[] storepass = keyStorePassword.toCharArray();
        char[] keypass = keyStorePassword.toCharArray();

        kmf = KeyManagerFactory.getInstance("SunX509");
        FileInputStream fin = new FileInputStream(keyStoreFilepath);
        ks = KeyStore.getInstance("PKCS12");
        ks.load(fin, storepass);

        kmf.init(ks, keypass);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
        sslFactory = sc.getSocketFactory();


    }

    private void initializeSocketFactory() throws Exception{
        System.setProperty("javax.net.ssl.keyStore", keyStoreFilepath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
        System.setProperty("javax.net.ssl.trustStore", trustStoreFilepath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
        sslFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    }
}
