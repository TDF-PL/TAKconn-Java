namespace WOT.TAK.Connection
{
    public class ConnectorSettings
    {
        public string ServerUrl { get; set; } = string.Empty;

        public string ServerPort { get; set; } = string.Empty;

        public string CotDirPath { get; set; } = string.Empty;

        public string CotResponsesDirPath { get; set; } = string.Empty;

        public bool CertificateVerification { get; set; } = false;

        public string TrustStorePath { get; set; } = string.Empty;

        public string TrustStorePassword { get; set; } = string.Empty;

        public string KeyStorePath { get; set; } = string.Empty;

        public string KeyStorePassword { get; set; } = string.Empty;

        public ConnectionMode ConnectionMode { get; set; }

        public string Login { get; set; } = string.Empty;

        public string Password { get; set; } = string.Empty;
    }

}