import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class Program {

    public static void main(String[] args) {
        try {
            Cipher cipher = GetCipher();

            String plainText = "hello world!";
            byte[] plainTextBytes = plainText.getBytes();

            byte[] encryptedBytes = cipher.doFinal(plainTextBytes);

            String encryptedBase64 = new String(Base64.getEncoder().encode(encryptedBytes));
            System.out.println(encryptedBase64);

        } catch (Exception ex) {
            System.out.println(ex.fillInStackTrace());
        }
    }

    private static Cipher GetCipher() {
        Cipher cipher = null;
        try {
            // base 64 encoded exponent and modulus
            String exponentBase64 = "PUT THE EXPONENT WE SENT TO YOU HERE";
            String modulusBase64 = "PUT THE MOUDULUS WE SENT TO YOU HERE";

            byte[] exponentBytes = Base64.getDecoder().decode(exponentBase64.getBytes());
            byte[] modulusBytes = Base64.getDecoder().decode(modulusBase64.getBytes());

            BigInteger exponent = new BigInteger(1, exponentBytes);
            BigInteger modulus = new BigInteger(1, modulusBytes);

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);

            // ALTERNATIVE WAY OF LOADING THE PUBLIC KEY
            // String contents = "PUT THE PUBLIC KEY WE SENT TO YOU HERE";
            // contents = contents.replace("-----BEGIN RSA PUBLIC KEY-----", "");
            // contents = contents.replace("-----END RSA PUBLIC KEY-----", "");
            // byte[] prepared =
            // com.sun.org.apache.xml.internal.security.utils.Base64.decode(contents);
            // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(prepared);

            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);

            // needed to fix a java compatibility issue.
            // https://github.com/palantir/encrypted-config-value/issues/58
            OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"),
                    PSource.PSpecified.DEFAULT);

            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey, oaepParams);

        } catch (Exception ex) {

        }

        return cipher;
    }
}