namespace WOT.TAK.Connection;

public interface TAKServerConnector
{
    public void SendFile(string filename);
    public void Connect();
    public void Close();
}
