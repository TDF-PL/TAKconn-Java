using System.Net.Security;
using System.Net.Sockets;
using System.Security.Cryptography.X509Certificates;


namespace WOT.TAK.Connection.src
{
    internal class Authentication
    {
        private X509Certificate2 _certificate;
        private X509Certificate2Collection _collection;

        public Authentication(string certPath, string storeName)
        {
            _certificate = new X509Certificate2(certPath, "passphrase");
            _collection = new X509Certificate2Collection(_certificate);
        }
        public bool AuthenticateX509(string serverName, TcpClient client)
        {
            Console.WriteLine(_certificate);
            SslStream sslStream;
            try
            {
                sslStream = new SslStream(
                    client.GetStream(),
                    false,
                    new RemoteCertificateValidationCallback(ValidateRemoteCertificate),
                    null
                    );
                Console.WriteLine(sslStream.IsMutuallyAuthenticated);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: {0}", ex.ToString());
                return false;
            }
            try
            {
                sslStream.AuthenticateAsClient(
                    serverName,
                    _collection,
                    false);
                Console.WriteLine("test");
                Console.WriteLine(sslStream.IsAuthenticated);
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: {0}", ex.ToString());
                return false;
            }
        }
        private bool ValidateRemoteCertificate(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors sslPolicyErrors)
        {
            Console.WriteLine("test1");
            if (sslPolicyErrors == SslPolicyErrors.None)
            {
                return true;
            }
            return false;
        }
    }
}