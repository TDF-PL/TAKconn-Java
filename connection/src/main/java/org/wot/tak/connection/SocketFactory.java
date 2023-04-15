package org.wot.tak.connection;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
import java.security.Security;
import java.util.*;

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

        initializeSocketFactory(trustAllCerts);
    }

    private void initializeSocketFactory(TrustManager[] tm) throws Exception {
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] storepass = cfg.getKeyStorePassword().toCharArray();

        kmf = KeyManagerFactory.getInstance("SunX509");
        InputStream fin = this.getClass().getClassLoader().getResourceAsStream(cfg.getKeyStorePath());
        Objects.requireNonNull(fin);
        ks = KeyStore.getInstance("PKCS12");
        ks.load(fin, storepass);
        kmf.init(ks, storepass);

        if (tm == null) {
            TrustManagerFactory tmf;
            KeyStore ts;
            char[] trustpass = cfg.getTrustStorePassword().toCharArray();
            tmf = TrustManagerFactory.getInstance("SunX509");
            fin = this.getClass().getClassLoader().getResourceAsStream(cfg.getTrustStorePath());
            Objects.requireNonNull(fin);
            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            ts = KeyStore.getInstance("PKCS12", "BC");
            ts.load(fin, trustpass);
            tmf.init(ts);
            tm = tmf.getTrustManagers();
        }

        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(kmf.getKeyManagers(), tm, new java.security.SecureRandom());
        sslFactory = sc.getSocketFactory();


    }

    private void initializeSocketFactory() throws Exception {
        initializeSocketFactory(null);
    }
}
