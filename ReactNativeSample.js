import {
    Rokt,
    RoktEmbeddedView,
    RoktEventManager
} from "@rokt/react-native-sdk";

var forge = require('node-forge');

let ENCRYPTION_KEY_ID = "YOUR_ENCRIPTION_KEY_ID" // Provided by Rokt
let RSA_PUBLIC_KEY = "YOUR_PUBLIC_KEY"

// Encrypt RSA
encrypt(text, publicKey) {
    var publicBytes = forge.util.decode64(publicKey);
    var pkeyAsn1 = forge.asn1.fromDer(publicBytes);
    var publicKey = forge.pki.publicKeyFromAsn1(pkeyAsn1);
    let toEncrypt = Buffer.from(text);
    let encrypted = publicKey.encrypt(toEncrypt, 'RSA-OAEP', {
        md: forge.md.sha256.create()
    });
    return forge.util.encode64(encrypted);
}


executeEncrypted = async () => {
    try {
        var attributes = {
            postcode: "90210"
        };

        // Encrypt PII data
        attributes["emailEnc"] = this.encrypt("jenny@example.com", RSA_PUBLIC_KEY).toString();
        attributes["firstnameEnc"] = this.encrypt("jenny", RSA_PUBLIC_KEY).toString();

        Rokt.execute(this.state.viewName, attributes, placeholders, (onLoad) => {
            console.log("Widget OnLoad Callback");
        });
    } catch (e) {
        console.error(e);
    }
};
