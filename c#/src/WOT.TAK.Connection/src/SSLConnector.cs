using System.Net.Security;
using System.Net.Sockets;
using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using System.Text;

namespace WOT.TAK.Connection
{
    public class SSLConnector : TAKServerConnector
    {
        private string _serverUrl;
        private int _serverPort;
        private string _responseStoragePath;
        private SslStream _sslStream;
        private TcpClient _client;

        internal SSLConnector(string serverUrl, string serverPort, string responseStoragePath)
        {
            this._serverUrl = serverUrl;
            this._serverPort = int.Parse(serverPort);
            this._responseStoragePath = responseStoragePath;
        }

        public void Close()
        {
            _client.Close();
        }

        public void Connect()
        {
            TcpClient _client = new TcpClient(_serverUrl, _serverPort);
            Console.WriteLine("Client connected.");
            _sslStream = new SslStream(
                _client.GetStream(),
                false,
                new RemoteCertificateValidationCallback(ValidateServerCertificate),
                null
            );
            try
            {
                _sslStream.AuthenticateAsClient(_serverUrl);
            }
            catch (AuthenticationException e)
            {
                Console.WriteLine("Exception: {0}", e.Message);
                if (e.InnerException != null)
                {
                    Console.WriteLine("Inner exception: {0}", e.InnerException.Message);
                }
                Console.WriteLine("Authentication failed - closing the connection.");
                _client.Close();
                return;
            }
        }

        public void SendFile(string path)
        {
            StringBuilder msg = new StringBuilder();
            using (StreamReader file = new(path))
            {
                string line;
                while ((line = file.ReadLine()) != null)
                {
                    msg.Append(line);
                }
                file.Close();
            }
            byte[] data = Encoding.ASCII.GetBytes(msg.ToString());
            _sslStream.Write(data);
            _sslStream.Flush();
            string serverMessage = ReadMessage(_sslStream);
            using (StreamWriter sw = File.CreateText(_responseStoragePath + "/" + DateTimeOffset.Now.ToUnixTimeMilliseconds() + ".cot"))
            {
                sw.Write(serverMessage);
            }
        }
        public static bool ValidateServerCertificate(
             object sender,
             X509Certificate certificate,
             X509Chain chain,
             SslPolicyErrors sslPolicyErrors)
        {
            if (sslPolicyErrors == SslPolicyErrors.None)
                return true;

            Console.WriteLine("Certificate error: {0}", sslPolicyErrors);

            return false;
        }
        static string ReadMessage(SslStream sslStream)
        {
            byte[] buffer = new byte[2048];
            StringBuilder messageData = new StringBuilder();
            int bytes = -1;
            do
            {
                bytes = sslStream.Read(buffer, 0, buffer.Length);

                Decoder decoder = Encoding.UTF8.GetDecoder();
                char[] chars = new char[decoder.GetCharCount(buffer, 0, bytes)];
                decoder.GetChars(buffer, 0, bytes, chars, 0);
                messageData.Append(chars);
                if (messageData.ToString().IndexOf("<EOF>") != -1)
                {
                    break;
                }
            } while (bytes != 0);

            return messageData.ToString();
        }
    }
}