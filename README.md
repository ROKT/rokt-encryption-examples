# rokt-encryption-examples

In this repository, some encryption examples provided in different programming languages.

## C#

[NetCoreEncryptionSample](./c_sharp/NetCoreEncryptionSample.cs)

## Golang

[GolangEncryptionSample](./golang/GolangEncryptionSample.go)

## Java

[JavaEncryptionSample](./java/JavaEncryptionSample.java)

## JavaScript

[JavaScriptSample-nodeforge](./javascript/JavaScriptSample-nodeforge.html)

[JavaScriptSample-subtlecrypto](./javascript/JavaScriptSample-subtlecrypto.html)

[NodeJsNodeRsaEncryptionSample](./javascript/NodeJsNodeRsaEncryptionSample.js)

[NodeJsNodeForgeEncryptionSample](./javascript/NodeJsNodeForgeEncryptionSample.js)

[ReactNativeSample](./javascript/ReactNativeSample.js)

## Kotlin

[KotlinEncryptionSample](./kotlin/KotlinEncryptionSample.kt)

By default, Rokt provides partners with an RSA public key. To use this key with the above Kotlin encryption example, please first convert your RSA public key to an x.509 public key with the following steps:
- Ensure that the Base64 section of the key has a line break every 80 characters
- `openssl rsa -pubin -in /tmp/k.rsa -RSAPublicKey_in -inform pem`

## Swift

[SwiftEncryptionSample](./swift/SwiftEncryptionSample.swift)

### License

Please see [LICENSE](LICENSE)
