namespace WOT.TAK.Connection
{
    internal class SocketFactory
    {
        private readonly ConnectorSettings _settings;

        public SocketFactory(ConnectorSettings settings)
        {
            this._settings = settings;
        }
    }
}