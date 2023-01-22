package org.wot.tak.connection;

import java.io.File;

public interface TAKServerConnector {
    public void sendFile(File file) throws Exception;
    public void connect() throws Exception;
    public void close() throws Exception;
}
