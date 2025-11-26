package com.theworkers.usermicroservice.util;

import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Base64;

@Component
public class CryptoHelper {
    public record StringKeyPair(String publicKey, String privateKey) {}

    public StringKeyPair generateRSAKeys() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);

            // 2. Generar el par
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            // 3. Convertir a Base64
            // getEncoded() devuelve el formato estándar (PKCS#8 para privada, X.509 para pública)
            String privBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            String pubBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            return new StringKeyPair(pubBase64, privBase64);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al inicializar algoritmo de encripción: " + e.getMessage());
        }
    }

}
