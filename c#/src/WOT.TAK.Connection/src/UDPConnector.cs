using System.Net.Sockets;
using System.Text;

namespace WOT.TAK.Connection
{
    public class UDPConnector : TAKServerConnector
    {
        private readonly string _url;
        private readonly int _port;

        private UdpClient _socket;

        public UDPConnector(string url, string port)
        {
            _url = url;
            _port = int.Parse(port);
            _socket = new UdpClient();
        }

        public void Connect()
        {
            try
            {
                _socket.Connect(_url, _port);
            }
            catch (Exception e ) 
            {
                Console.WriteLine(e.Message);
            }
        }   

        public void SendFile(string filename)
        {
            StringBuilder msg = new StringBuilder();
            using (StreamReader file = new(filename))
            {
                string line;
                while ((line = file.ReadLine()) != null)
                {
                    msg.Append(line);
                }
                file.Close();
            }
            byte[] data = Encoding.ASCII.GetBytes(msg.ToString());
            _socket.Send(data, data.Length);
        }
        public void Close()
        {
            _socket.Close();
        }
    }
}
