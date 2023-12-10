package org.wot.tak.connection.infra;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.wot.tak.common.Port;
import org.wot.tak.common.Url;
import org.wot.tak.connection.configuration.SocketFactoryConfig;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Security;
import java.util.Objects;

public final class SocketFactory {
    private final SocketFactoryConfig config;

    public SocketFactory(SocketFactoryConfig configuration) throws Exception {
        this.config = configuration;
    }

    public Socket createSSLSocket(Url url, Port port) throws Exception {
        var sslFactory = this.config.getServerCertVerification()
                ? initializeSocketFactory()
                : initializeTestSocketFactory();

        return sslFactory.createSocket(url.getUrl(), port.getNumber());
    }

    public Socket createSocket(Url url, Port port) throws Exception {
        return new Socket(url.getUrl(), port.getNumber());
    }

    private SSLSocketFactory initializeTestSocketFactory() throws Exception {
        var trustAllCerts = new TrustManager[]{
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

        return initializeSocketFactory(trustAllCerts);
    }

    private SSLSocketFactory initializeSocketFactory(TrustManager[] trustManagers) throws Exception {
        var storepass = config.getKeyStorePassword().toCharArray();

        var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        var inputStream = this.getClass().getClassLoader().getResourceAsStream(config.getKeyStorePath());
        Objects.requireNonNull(inputStream);
        var keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(inputStream, storepass);
        keyManagerFactory.init(keyStore, storepass);

        if (trustManagers == null) {
            TrustManagerFactory trustManagerFactory;
            KeyStore keyStore2;
            var trustStorePassword = config.getTrustStorePassword().toCharArray();
            trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            inputStream = this.getClass().getClassLoader().getResourceAsStream(config.getTrustStorePath());
            Objects.requireNonNull(inputStream);
            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            keyStore2 = KeyStore.getInstance("PKCS12", "BC");
            keyStore2.load(inputStream, trustStorePassword);
            trustManagerFactory.init(keyStore2);
            trustManagers = trustManagerFactory.getTrustManagers();
        }

        var sc = SSLContext.getInstance("TLSv1.2");
        sc.init(keyManagerFactory.getKeyManagers(), trustManagers, new java.security.SecureRandom());
        return sc.getSocketFactory();
    }

    private SSLSocketFactory initializeSocketFactory() throws Exception {
        return initializeSocketFactory(null);
    }
}
