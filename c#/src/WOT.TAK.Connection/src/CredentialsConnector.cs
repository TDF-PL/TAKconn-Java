namespace WOT.TAK.Connection
{
    internal class CredentialsConnector : TAKServerConnector
    {
        private string serverUrl;
        private string serverPort;
        private string cotResponsesDirPath;
        private SocketFactory socketFactory;
        private string login;
        private string password;

        public CredentialsConnector(string serverUrl, string serverPort, string cotResponsesDirPath, SocketFactory socketFactory, string login, string password)
        {
            this.serverUrl = serverUrl;
            this.serverPort = serverPort;
            this.cotResponsesDirPath = cotResponsesDirPath;
            this.socketFactory = socketFactory;
            this.login = login;
            this.password = password;
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