using Microsoft.Extensions.Options;

namespace WOT.TAK.Connection;

public class ConnectorFactory
{
    private readonly ConnectorSettings _settings;

    public ConnectorFactory(IOptionsSnapshot<ConnectorSettings> settings)
    {
        _settings = settings.Value;
    }

    public TAKServerConnector GetSSLConnector()
    {
        return new SSLConnector(
            _settings.ServerUrl,
            _settings.ServerPort,
            _settings.CotResponsesDirPath);
           // new SocketFactory(_settings));
    }

    public TAKServerConnector GetTAKServerConnector()
    {
        return new CredentialsConnector(
            _settings.ServerUrl,
            _settings.ServerPort,
            _settings.CotResponsesDirPath,
            new SocketFactory(_settings),
            _settings.Login,
            _settings.Password);
    }

    public TAKServerConnector GetPlainConnector()
    {
        return new PlainConnector(
            _settings.ServerUrl,
            _settings.ServerPort,
            _settings.CotResponsesDirPath,
            new SocketFactory(_settings));
    }

    public TAKServerConnector GetUDPConnector()
    {
        return new UDPConnector(
            _settings.ServerUrl,
            _settings.ServerPort);
    }
    public TAKServerConnector GetTCPConnector()
    {
        return new TCPConnector(
            _settings.ServerUrl,
            _settings.ServerPort,
            _settings.CotResponsesDirPath);
         //   new SocketFactory(_settings));
    }
}
