using System.Security.Cryptography;
using PgpCore;

namespace Integrity;
class Integrity{

    public void generateKey(){
        using (PGP pgp = new PGP())
        {
	        pgp.GenerateKey(@"C:\Users\Jakub\Documents\Integrity\Certs\public_cert\publicopgp.asc", @"C:\Users\Jakub\Documents\Integrity\Certs\public_cert\privateopgp.asc", "email@email.com", "password");
        }
    }

    public void signFile(){
        // Load keys
        FileInfo privateKey = new FileInfo(@"C:\Users\Jakub\Documents\Integrity\Certs\public_cert\privateopgp.asc");
        EncryptionKeys encryptionKeys = new EncryptionKeys(privateKey, "password");

        // Reference input/output files
        FileInfo inputFile = new FileInfo(@"C:\Users\Jakub\Documents\Integrity\test.txt");
        FileInfo signedFile = new FileInfo(@"C:\Users\Jakub\Documents\Integrity\test.pgp");

        // Sign
        PGP pgp = new PGP(encryptionKeys);
        pgp.SignFile(inputFile, signedFile);
    }
    public bool verifyPgp(String filename){
        FileInfo publicKey = new FileInfo("Certs\\public_cert\\publicopgp.asc");
        EncryptionKeys encryptionKeys = new EncryptionKeys(publicKey);
        FileInfo inputFile = new FileInfo(filename);
        PGP pgp = new PGP(encryptionKeys);
        return pgp.VerifyFile(inputFile);
        // var task = pgp.VerifyFileAsync(inputFile);
        // var result = Task.WhenAll<bool>(task).Result;
        // return result[0];
    }
    public String getHash(String filename){
        //byte[] hash;
        SHA1Managed sha = new SHA1Managed();
        using (sha){
            using (var fileStream = new FileStream(filename, 
                                               FileMode.Open, 
                                               FileAccess.Read, 
                                               FileShare.ReadWrite))
            {
                var hash = sha.ComputeHash(fileStream);
                var hashString = Convert.ToBase64String(hash);
                Console.WriteLine(hashString);
                return hashString.TrimEnd('=');
            }
        }
    }    
    public String search(String text, String filename, int b){
        const string dataFolder = "Data"; //todo
        String end_filename = "APP6B_to_CoT.csv";
        String line = null;
        
        //Selecting file,  default is APPd6B_to_CoT.csv
        if(String.Compare(filename,"CoT_to_APP6B.csv") == 0){
            end_filename = "CoT_to_APP6B.csv";
        }
        try{
        StreamReader readFile = new StreamReader(end_filename);
        readFile.ReadLine();
        while ((line = readFile.ReadLine()) != null)
        {
            if(String.Compare(text,line.Split(";")[0]) == 0) break;
        }
        readFile.Close();
        }catch(Exception e){
            Console.WriteLine("A reading file problem.\n"+e.Message);
            Environment.Exit(1);
        }
        if(line == null) throw new Exception("File not found in .csv");
        return(line.Split(";")[2]);
    }

}
class Program{
    static void Main(string[] args){
        var t = new Integrity() ;

        try{
            String line = t.search("SFA-MHUH--","test",1);
            Console.WriteLine("Match: " + line);
        }catch(Exception e){
            Console.WriteLine(e.Message);
        }

        Console.WriteLine(t.getHash("CoT_to_APP6B.csv"));
        t.signFile();
        //t.generateKey();
        Console.WriteLine(t.verifyPgp("test.pgp"));
        //Console.WriteLine(t.verifyPgp("Certs\\CoT_to_APP6B.csv.pgp"));
    }
}
