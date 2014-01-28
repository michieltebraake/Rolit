package util;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Crypto {

    /**
     * Decodes a base64 string to bytes.
     *
     * @param message base64 string in plain text
     * @return decoded base64 string
     */
    public static byte[] decodeBase64(String message) {
        return DatatypeConverter.parseBase64Binary(message);
    }

    /**
     * Encodes a byte array to a base64 string.
     *
     * @param bytes byte array to be encoded
     * @return base64 string
     */
    public static String encodeBase64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    /**
     * Generates a PrivateKey Object out of raw byte[] material.
     *
     * @param rawKey byte array containing the privatekey.
     * @return PrivateKey object
     */
    public static PrivateKey decodePrivateKey(byte[] rawKey) {
        PKCS8EncodedKeySpec keySpec;
        KeyFactory fact;
        PrivateKey privateKey = null;
        try {
            keySpec = new PKCS8EncodedKeySpec(rawKey);
            fact = KeyFactory.getInstance("RSA");
            privateKey = fact.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * Generates a PubliceKey Object out of raw byte[] material.
     *
     * @param rawKey byte array containing the publickey.
     * @return PublicKey object
     */
    public static PublicKey decodePublicKey(byte[] rawKey) {
        X509EncodedKeySpec keySpec;
        KeyFactory fact;
        PublicKey publicKey = null;
        try {
            keySpec = new X509EncodedKeySpec(rawKey);
            fact = KeyFactory.getInstance("RSA");
            publicKey = fact.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * Signs a message with a PrivateKey.
     *
     * @param message    the message to be signed
     * @param privateKey the Privatekey to be used
     * @return signature message
     */
    public static byte[] signMessage(String message, PrivateKey privateKey) {
        Signature sig;
        byte[] signature = null;
        try {
            sig = Signature.getInstance("SHA1withRSA");
            sig.initSign(privateKey);
            sig.update(message.getBytes());
            signature = sig.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return signature;
    }

    /**
     * Checks if a message encrypts to a pre-defined encrypted message (signature).
     *
     * @param signature the message signed with a PrivateKey
     * @param message   the message in plain text
     * @param publicKey the PublicKey to sign the plain text message with
     * @return true if the message signs to the given signature using the Publickey. returns false otherwise.
     */
    public static boolean verifyToken(byte[] signature, String message, PublicKey publicKey) {
        Boolean check = false;
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(message.getBytes());
            check = sig.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return check;
    }


    /**
     * Generates a SecureRandom String
     *
     * @return SecureRandom String
     */
    public static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(200, secureRandom).toString(32);
    }
}
