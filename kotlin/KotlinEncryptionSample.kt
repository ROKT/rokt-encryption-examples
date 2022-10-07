import java.util.Base64
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

const val ENCRYPTION_KEY_ID= "YOUR_ENCRYPTION_KEY_ID" // Provided by Rokt

// RSA_PUBLIC_KEY should be your RSA public key, which is a long string similar to this example:
const val RSA_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApyAEV2RbqhRDM6eyb2qh8qLejdp7g1HwnxpbNS6CCrJKkyWsl3S2mQSvgaoxMELRWQNL77Pn2BWLC7zEn/qwi2Hcu5VS6+7z08RgDGSjmr9JOZahhWFjVyiZfePr+OBrhmz5RaSnH33d14Y4+rbK/Ypk7NVyNfiCZohAOZYdIALcImWUpivKUKgzy3YtLkhDl92wxOPIQcclCsx2keGhxiSkhkSZGMnvkm5Pmt6W6ISJEphnIx4e9irWNM+l57PXmytdQ91bSasYJfo7UckMvb4Jb9gh4+BnCGyN0zau+womqD4ZUj0QY30HdwanFftjVs/jI6hDHYlVZO+I50FspQIDAQAB"

// Sample RSA encryption
fun String.rsa(publicKey: String): String? {
    try {
        val publicBytes: ByteArray = Base64.getDecoder().decode(publicKey)
        val keySpec = X509EncodedKeySpec(publicBytes)
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        val pubKey: PublicKey = keyFactory.generatePublic(keySpec)
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding").apply {
            init(Cipher.ENCRYPT_MODE, pubKey)
        }
        val encrypted = cipher.doFinal(this.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

// Sample sha256 hash
fun String.sha256(): String {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}

// Complete execute function with RSA encrypted firstname and email
fun executeEncrypted() {
    val placeholders = hashMapOf(
        Pair("Location1", WeakReference(widget_location_1))
    )

    val attributes = hashMapOf(
        Pair("postcode", "90210"),
        Pair("country", "AU")
    )

    // Encrypt PII using public KEY
    attributes["emailEnc"] = "jenny@example.com".rsa(RSA_PUBLIC_KEY).orEmpty()
    attributes["firstnameEnc"] = "jenny".rsa(RSA_PUBLIC_KEY).orEmpty()

    // Make sure you include your Encryption Key ID
    attributes["piiencryptionkeyid"] = ENCRYPTION_KEY_ID

    // Execute
    Rokt.execute("YOUR_PAGE_IDENTIFIER", attributes, this, placeholders)
}
