using System.Net.Sockets;

namespace WOT.TAK.Connection
{
    public class TCPConnector : TAKServerConnector
    {
        private String _url;
        private int _port;
        private String _responseStoragePath;
        private TcpClient _socket;
        protected StreamReader _input;
        protected StreamWriter _output;
        private Thread _listener;
        private bool _run;
        public TCPConnector(String url, String port, String responseStoragePath)
        {
            _url = url;
            _port = Int32.Parse(port);
            _responseStoragePath = responseStoragePath;
            _socket = new TcpClient();
            _run = true;
        }

        public void SendFile(string fileName)
        {
            try
            {
                using (StreamReader sr = new StreamReader(fileName))
                {
                    string line;
                    while ((line = sr.ReadLine()) != null)
                    {
                        _output.WriteLine(line);
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        public void Connect()
        {
            try
            {
                _socket.Connect(_url, _port);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
            _output = new StreamWriter(_socket.GetStream());
            _input = new StreamReader(_socket.GetStream());
            _socket.ReceiveTimeout = 1000;
            _listener = new Thread(new ThreadStart(ResponseListener));
            _listener.Start();
        }
        private void ResponseListener()
        {
            string line;
            while(_run)
            {
                try
                {
                    line = _input.ReadLine();
                    using (StreamWriter sw = File.CreateText(_responseStoragePath + "/" + DateTimeOffset.Now.ToUnixTimeMilliseconds() + ".cot"))
                    {
                        sw.WriteLine(line);
                    }
                }
                catch (SocketException e)
                {
                    continue;
                }
                catch (Exception ex)
                {
                    Console.Error.WriteLine(ex.Message);
                }
            }
        }
        public void Close()
        {
            _run = false;
            Thread.Sleep(200);
            _output.Close();
            _input.Close();
            _socket.Close();
        }

        public TcpClient GetSocket() { return _socket; }
    }
}
