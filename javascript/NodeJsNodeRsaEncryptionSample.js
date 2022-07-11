const NodeRSA = require('node-rsa');

function encrypt(text) {
    let publicKeyText = `-----BEGIN RSA PUBLIC KEY-----
    PUBLIC KEY HERE
    -----END RSA PUBLIC KEY-----`;
    const key = new NodeRSA(publicKeyText, 'pkcs1-public-pem', {
        environment: 'browser',
        encryptionScheme: {
            scheme: 'pkcs1_oaep',
            hash: 'sha256'
        }
    });
    return key.encrypt(text, 'base64');
}

var hw = encrypt("Some serious stuff");
console.log(hw);