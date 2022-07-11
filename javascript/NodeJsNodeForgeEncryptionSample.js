var forge = require('node-forge');
var fs = require("fs");

function encrypt(text) {
    let publicKeyText = `-----BEGIN RSA PUBLIC KEY-----
    PUBLIC KEY HERE
    -----END RSA PUBLIC KEY-----`;
    let publicKey = forge.pki.publicKeyFromPem(publicKeyText);
    let toEncrypt = Buffer.from(text);
    let encrypted = publicKey.encrypt(toEncrypt, 'RSA-OAEP', { md: forge.md.sha256.create() });
    return forge.util.encode64(encrypted);
}

var hw = encrypt("example@mail.com");
console.log(hw);