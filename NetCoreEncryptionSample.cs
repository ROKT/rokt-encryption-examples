// base 64 encoded exponent and modulus
var modulus = "PUT THE MODULUS WE SENT TO YOU HERE";
var exponent = "PUT THE EXPONENT WE SENT TO YOU HERE";

rsa.ImportParameters (new RSAParameters {
    Exponent = Convert.FromBase64String (exponent), Modulus = Convert.FromBase64String (modulus)
});

// ALTERNATIVE WAY OF LOADING THE PUBLIC KEY 
// using var fileStream = File.Open(publicKeyFile, FileMode.Open);
// using var pemReader = new PemReader(fileStream);
//
// rsa.ImportParameters(pemReader.ReadRsaKey());

var text = "test@test.com";
var bytes = Encoding.UTF8.GetBytes (text);
return Convert.ToBase64String (rsa.Encrypt (bytes, RSAEncryptionPadding.OaepSHA256));