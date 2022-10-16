namespace WOT.TAK.Connection
{
    internal class PlainConnector : TAKServerConnector
    {
        private string serverUrl;
        private string serverPort;
        private string cotResponsesDirPath;
        private SocketFactory socketFactory;

        public PlainConnector(string serverUrl, string serverPort, string cotResponsesDirPath, SocketFactory socketFactory)
        {
            this.serverUrl = serverUrl;
            this.serverPort = serverPort;
            this.cotResponsesDirPath = cotResponsesDirPath;
            this.socketFactory = socketFactory;
        }

        public void Close()
        {
            throw new NotImplementedException();
        }

        public void Connect()
        {
            throw new NotImplementedException();
        }

        public void SendFile(string path)
        {
            throw new NotImplementedException();
        }
    }
}