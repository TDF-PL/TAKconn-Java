using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Options;
using System.Net.Security;
using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using WOT.TAK.Connection;

public class Program
{
    static void Main(string[] args)
    {
        using var host = CreateHostBuilder().Build();

        // Invoke Worker
        using IServiceScope serviceScope = host.Services.CreateScope();
        IServiceProvider provider = serviceScope.ServiceProvider;
        var client = provider.GetRequiredService<Client>();
        client.DoWork();

        host.Run();
    }

    static IHostBuilder CreateHostBuilder()
    {
        return Host.CreateDefaultBuilder()
            .ConfigureAppConfiguration((hostingContext, configuration) =>
            {
                configuration.AddJsonFile("appsettings.json",false, true);
            })
            .ConfigureServices((builder, services) =>
            {
                services.AddSingleton<Client>()
                .AddSingleton<ConnectorFactory>()
                .Configure<ConnectorSettings>(builder.Configuration);
            });
    }
}

// Worker.cs
internal class Client
{
    private readonly ConnectorFactory _connectorFactory;

    public Client(ConnectorFactory connectorFactory)
    {
        _connectorFactory = connectorFactory;
    }

    public void DoWork()
    {
        var connector = new CertificateConnector("212.160.99.185", "8089", null, @"~\mwolski1.p12", @"ZawszeBlisko2021");
        connector.Connect();
        connector.SendFile("cot/messages/msg1.cot");
        connector.SendFile("cot/messages/msg2.cot");
        connector.SendFile("cot/messages/msg3.cot");
        connector.Close();
        Console.WriteLine("Sent");
    }
   

}