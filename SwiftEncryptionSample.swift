import Foundation
import CommonCrypto

let ENCRYPTION_KEY_ID = "YOUR_ENCRIPTION_KEY_ID" // Provided by Rokt
let RSA_PUBLIC_KEY = "YOUR_PUBLIC_KEY"

public func getAttributes() -> [String: String] {
    var attributes = ["postcode": "90210"]
    
    // Encrypt PII using public KEY
    attributes["firstnameEnc"] = "Jenny".encryptRSA(publicKey: RSA_PUBLIC_KEY)
    
    // Make sure you include your Encryption Key ID
    attributes["piiencryptionkeyid"] = ENCRYPTION_KEY_ID
    
    return attributes
}

public extension String {
    @available(iOS 10.0, *)
    func encryptRSA(publicKey: String) -> String? {
        let keyString = publicKey.replacingOccurrences(of: "-----BEGIN PUBLIC KEY-----\n", with: "")
        .replacingOccurrences(of: "\n-----END PUBLIC KEY-----", with: "")
        guard let keyData = Data(base64Encoded: keyString) else {
            return nil
        }
        var attributes: CFDictionary {
            return [kSecAttrKeyType         : kSecAttrKeyTypeRSA,
                    kSecAttrKeyClass        : kSecAttrKeyClassPublic,
                    kSecAttrKeySizeInBits   : 2048,
                    kSecReturnPersistentRef : kCFBooleanTrue ?? true as Any] as CFDictionary
        }
        var error: Unmanaged<CFError>? = nil
        guard let secKey = SecKeyCreateWithData(keyData as CFData, attributes, &error) else {
            return nil
        }
        return encryptRSA(publicKey: secKey)
    }

    @available(iOS 10.0, *)
    func encryptRSA(publicKey: SecKey) -> String? {
        let error:UnsafeMutablePointer<Unmanaged<CFError>?>? = nil
        if let stringData = self.data(using: .utf8),
        let encryptedMessageData:Data = SecKeyCreateEncryptedData(publicKey, .rsaEncryptionOAEPSHA256,
                                                                  stringData as CFData,error) as Data? {
            return encryptedMessageData.base64EncodedString()
        }
        return nil
    }

}
