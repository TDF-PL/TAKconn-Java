package org.wot.tak.connection;

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
    private final SocketFactoryConfig cfg;
    private SSLSocketFactory sslFactory;

    protected SocketFactory(SocketFactoryConfig cfg) throws Exception{
        this.cfg = cfg;
        if (cfg.getServerCertVerification()) {
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
        char[] storepass = cfg.getKeyStorePassword().toCharArray();
        char[] keypass = cfg.getKeyStorePassword().toCharArray();

        kmf = KeyManagerFactory.getInstance("SunX509");
        FileInputStream fin = new FileInputStream(cfg.getKeyStorePath());
        ks = KeyStore.getInstance("PKCS12");
        ks.load(fin, storepass);

        kmf.init(ks, keypass);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
        sslFactory = sc.getSocketFactory();


    }

    private void initializeSocketFactory() throws Exception{
        System.setProperty("javax.net.ssl.keyStore", cfg.getKeyStorePath());
        System.setProperty("javax.net.ssl.keyStorePassword", cfg.getKeyStorePassword());
        System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
        System.setProperty("javax.net.ssl.trustStore", cfg.getTrustStorePath());
        System.setProperty("javax.net.ssl.trustStorePassword", cfg.getTrustStorePassword());
        sslFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    }
}
