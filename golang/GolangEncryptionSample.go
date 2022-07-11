package main

import (
	"crypto/rand"
	"crypto/rsa"
	"crypto/sha256"
	"crypto/x509"
	"encoding/base64"
	"encoding/hex"
	"encoding/pem"
	"errors"
	"fmt"
	"log"
	"strings"
)

func main() {
	// Prepare the input parameters
	const (
		publicKeyText = `
-----BEGIN RSA PUBLIC KEY-----
XXXX
-----END RSA PUBLIC KEY-----
`
		sensitiveData = "user@site.com"
	)

	// Encode the sensitive input
	encoded, err := Base64EncodeEncryptedSHA256(sensitiveData, publicKeyText)
	if err != nil {
		log.Fatalf("failed to encrypt encoded payload: %v", err)
	}

	log.Println(encoded)
}

// Base64EncodeEncryptedSHA256 demonstrates how to transform a sensitive input
// into a hashed and Base64-encoded encrypted payload.
func Base64EncodeEncryptedSHA256(input, publicKeyText string) (string, error) {
	// Encode the e-mail as a SHA256 hexadecimal string
	trimmedInput := strings.TrimSpace(strings.ToLower(input))
	encryptedInputBytes := sha256.Sum256([]byte(trimmedInput))
	encryptedInputHex := hex.EncodeToString(encryptedInputBytes[:])

	// Decode the RSA public key
	block, _ := pem.Decode([]byte(publicKeyText))
	if block == nil {
		return "", errors.New("failed to parse PEM block containing the public key")
	}

	// Parse the decoded RSA public key
	parsedPublicKey, err := x509.ParsePKIXPublicKey(block.Bytes)
	if err != nil {
		return "", fmt.Errorf("failed to parse DER encoded public key: %w", err)
	}

	publicKey, ok := parsedPublicKey.(*rsa.PublicKey)
	if !ok {
		return "", fmt.Errorf("parsed unexpected key type %T", publicKey)
	}

	// Encrypt the hashed secret using the RSA public key
	encryptedBytes, err := rsa.EncryptOAEP(sha256.New(), rand.Reader, publicKey, []byte(encryptedInputHex), nil)
	if err != nil {
		return "", fmt.Errorf("failed to encrypt: %w", err)
	}

	// Base64 encode the encrypted payload
	encodedHash := base64.StdEncoding.EncodeToString(encryptedBytes[:])

	return encodedHash, nil
}
