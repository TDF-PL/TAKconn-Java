package wot.tak.client;

import wot.tak.connection.ConnectorFactoryConfig;
import wot.tak.connection.TAKServerConnector;
import wot.tak.connection.ConnectorFactory;
import wot.tak.validator.MessageValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class App {

    private static final String propertiesPath = "src/main/resources/config.properties";

    private static ConnectorFactory cFactory;
    private static MessageValidator messageValidator;

    public static void main(String[] args) {
        Properties prop;
        try {
            prop = initializeProperties();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        ConnectorFactoryConfig cFactoryConfig = new ConnectorFactoryConfig(
                prop.getProperty("cot.responses"),
                prop.getProperty("tak.server.url"),
                prop.getProperty("tak.server.port"),
                prop.getProperty("user.name"),
                prop.getProperty("user.password"),
                prop.getProperty("trust.store.path"),
                prop.getProperty("trust.store.password"),
                prop.getProperty("key.store.path"),
                prop.getProperty("key.store.password"),
                Boolean.parseBoolean(prop.getProperty("server.certificate.verification"))
        );
        cFactory = new ConnectorFactory(cFactoryConfig);
        try {
            messageValidator = new MessageValidator(prop.getProperty("cot.schema")  + "/COT.xsd");
            validateCOTMessages(prop.getProperty("cot.path"));

            // All .cot files will be sent to TAK server. Server response (if exist) will be recorded in separate files.
            sendFilesToTAKServer(prop.getProperty("connection.mode"), prop.getProperty("cot.path"));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static Properties initializeProperties() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(propertiesPath);
        prop.load(fis);
        return prop;
    }

    private static void sendFilesToTAKServer(String connectionMode, String cotDirectoryPath) throws Exception {
        TAKServerConnector connector = createConnector(connectionMode);
        sendFilesFromDirectoryUsingConnector(connector, cotDirectoryPath);
    }

    private static TAKServerConnector createConnector(String connectionMode) throws Exception {
        switch (connectionMode) {
            case "tls":
                return cFactory.getSSLConnector();
            case "tlsCred":
                return cFactory.getSSLCredConnector();
            case "credentials":
                return cFactory.getCredentialsConnector();
            case "udp":
                return cFactory.getUDPConnector();
            case "plain":
                return cFactory.getPlainConnector();
            default:
                throw new Exception("Unknown main.java.connection mode provided in configuration file");
        }
    }

    private static void validateCOTMessages(String cotDirectoryPath) throws Exception {
        File dir = new File(cotDirectoryPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    messageValidator.validateXMLSchema(file.getPath());
                } catch(Exception ex) {
                    throw new Exception("File: "+file.getName()+" invalid CoT message structure");
                }
            }
        }
    }

    private static void sendFilesFromDirectoryUsingConnector(TAKServerConnector connector, String cotDirectoryPath) throws Exception{
        connector.connect();
        File dir = new File(cotDirectoryPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                connector.sendFile(file);
            }
        }
        Thread.sleep(2000);
        connector.close();
    }

}
