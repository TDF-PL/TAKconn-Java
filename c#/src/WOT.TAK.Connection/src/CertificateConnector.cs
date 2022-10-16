using System.Net.Security;
using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using System.Text;

namespace WOT.TAK.Connection
{
    public class CertificateConnector : TAKServerConnector
    {
        protected string _serverUrl;
        protected string _serverPort;
        protected string _cotResponsesDirPath;
        protected SslStream _authConnection;
        protected string _certPath;
        protected string _certPass;
        public CertificateConnector(
            string serverUrl, 
            string serverPort, 
            string cotResponsesDirPath, 
            string certPath, 
            string certPass)
        {
            _serverUrl = serverUrl;
            _serverPort = serverPort;
            _cotResponsesDirPath = cotResponsesDirPath;
            _certPath = certPath;
            _certPass = certPass;
        }
        public void Close()
        {
            _authConnection.Close();
        }
        public void Connect()
        {
            TCPConnector connector = new TCPConnector(_serverUrl, _serverPort, null);
            connector.Connect();

            FileStream fileStream = File.OpenRead(_certPath);
            byte[] buffer = new byte[fileStream.Length];
            int bytesRead = fileStream.Read(buffer, 0, (int)fileStream.Length);

            X509Certificate2 clientCertificate = new X509Certificate2(buffer, _certPass);
            X509Certificate2Collection certificateCollection = new X509Certificate2Collection(clientCertificate);

            _authConnection = new SslStream(
                    connector.GetSocket().GetStream(),
                    false,
                    new RemoteCertificateValidationCallback(ValidateServerCertificate),
                    new LocalCertificateSelectionCallback(SelectUserCertificate));

            // Funkcja powodująca problemy
            _authConnection.AuthenticateAsClient(_serverUrl, certificateCollection, SslProtocols.Tls13, false);
        }
        public void SendFile(string path)
        {
            try
            {
                using (StreamReader sr = new StreamReader(path))
                {
                    string line;
                    while ((line = sr.ReadLine()) != null)
                    {
                        _authConnection.Write(Encoding.ASCII.GetBytes(line));
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
        private X509Certificate2 SelectUserCertificate(
            object sender,
            string targetHost,
            X509CertificateCollection collection,
            X509Certificate remoteCert,
            string[] acceptableIssuers)
        {
            FileStream fileStream = File.OpenRead(_certPath);
            byte[] buffer = new byte[fileStream.Length];
            int bytesRead = fileStream.Read(buffer, 0, (int)fileStream.Length);

            X509Certificate2 clientCertificate = new X509Certificate2(buffer, _certPass);
            X509Certificate2Collection certificateCollection = new X509Certificate2Collection(clientCertificate);
            return clientCertificate;
        }
        private bool ValidateServerCertificate(
             object sender,
             X509Certificate certificate,
             X509Chain chain,
             SslPolicyErrors sslPolicyErrors)
        {
            FileStream fileStream = File.OpenRead(_certPath);
            byte[] buffer = new byte[fileStream.Length];
            int bytesRead = fileStream.Read(buffer, 0, (int)fileStream.Length);

            X509Certificate2 clientCertificate = new X509Certificate2(buffer, _certPass);
            X509Certificate2Collection certificateCollection = new X509Certificate2Collection(clientCertificate);
            try
            {
                byte[] clientCertBytes = certificate.GetRawCertData();
                byte[] serverCertBytes = clientCertificate.GetRawCertData();
                if (clientCertBytes.Length != serverCertBytes.Length)
                {
                    throw new Exception("Client/server certificates do not match.");
                }
                for (int i = 0; i < clientCertBytes.Length; i++)
                {
                    if (clientCertBytes[i] != serverCertBytes[i])
                    {
                        throw new Exception("Client/server certificates do not match.");
                    }
                }
            }
            catch (Exception ex)
            {
                return false;
            }
            return true;
        }
    }
}