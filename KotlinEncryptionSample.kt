import android.util.Base64
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

const val ENCRYPTION_KEY_ID= "YOUR_ENCRIPTION_KEY_ID" // Provided by Rokt
const val RSA_PUBLIC_KEY = "YOUR_PUBLIC_KEY"

// Sample RSA encryption
fun String.rsa(publicKey: String): String? {
    try {
        val publicBytes: ByteArray = Base64.decode(publicKey, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(publicBytes)
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        val pubKey: PublicKey = keyFactory.generatePublic(keySpec)
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding").apply {
            init(Cipher.ENCRYPT_MODE, pubKey)
        }
        val encrypted = cipher.doFinal(this.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

// Sample sha256 Encryption
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
    attributes["emailEnc"] = "jenny@example.com".rsa(RSA_PUBLIC_KEY_PROD).orEmpty()
    attributes["firstnameEnc"] = "jenny".rsa(RSA_PUBLIC_KEY).orEmpty()

    // Make sure you include your Encryption Key ID
    attributes["piiencryptionkeyid"] = ENCRYPTION_KEY_ID

    // Execute
    Rokt.execute("YOUR_PAGE_IDENTIFIER", attributes, this, placeholders)
}
