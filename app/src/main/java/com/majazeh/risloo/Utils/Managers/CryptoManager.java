package com.majazeh.risloo.Utils.Managers;

import android.util.Base64;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CryptoManager {

    private final static String CRYPTO_METHOD = "RSA";
    private final static int CRYPTO_BITS = 2048;


    public static String encrypt(String text, String publicKey)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidKeySpecException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, stringToPublicKey(getMainPublicKey(publicKey)));
        try {
            byte[] data = text.getBytes();
            int chunkSize = 245; // 1024 / 8 - 11(padding) = 117
            int encSize = (int) (Math.ceil(data.length / 245.0) * 256);
            int idx = 0;
            ByteBuffer buf = ByteBuffer.allocate(encSize);
            while (idx < data.length) {
                int len = Math.min(data.length - idx, chunkSize);
                byte[] encChunk = cipher.doFinal(data, idx, len);
                buf.put(encChunk);
                idx += len;
            }

            // fully encrypted data
            byte[] encData = buf.array();
            return Base64.encodeToString(encData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String result, String privateKey)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            BadPaddingException,
            IllegalBlockSizeException,
            InvalidKeySpecException,
            InvalidKeyException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, stringToPrivateKey(getMainPrivateKey(privateKey)));

        int chunkSize = 256;
        int idx = 0;
        byte[] data = Base64.decode(result,1);
        ByteBuffer buf = ByteBuffer.allocate(data.length);
        while(idx < data.length) {
            int len = Math.min(data.length-idx, chunkSize);
            byte[] chunk = cipher.doFinal(data, idx, len);
            buf.put(chunk);
            idx += len;
        }

        byte[] decryptedData = buf.array();
        return new String(decryptedData).replaceAll("\u0000.*", "");
    }

    private static PublicKey stringToPublicKey(String publicKeyString)
            throws InvalidKeySpecException,
            NoSuchAlgorithmException {

        byte[] keyBytes = Base64.decode(publicKeyString, Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_METHOD);
        return keyFactory.generatePublic(spec);
    }

    private static PrivateKey stringToPrivateKey(String privateKeyString)
            throws InvalidKeySpecException,
            NoSuchAlgorithmException {

        byte[] pkcs8EncodedBytes = Base64.decode(privateKeyString, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance(CRYPTO_METHOD);
        return kf.generatePrivate(keySpec);
    }

    private static String getMainPublicKey(String pubKey) {
        String publicKey = "";
        pubKey.trim();
        if (pubKey.startsWith("-----BEGIN PUBLIC KEY-----") && pubKey.endsWith("-----END PUBLIC KEY-----")) {
            publicKey = pubKey.substring(26, pubKey.length() - 24);
        } else {
            publicKey = pubKey;
        }
        return publicKey;
    }

    private static String getMainPrivateKey(String privateKey) {
        String priKey = "";
        privateKey.trim();
        if (privateKey.startsWith("-----BEGIN RSA PRIVATE KEY-----") && privateKey.endsWith("-----END RSA PRIVATE KEY-----")) {
            priKey = privateKey.substring(31, privateKey.length() - 29);
        } else {
            priKey = privateKey;
        }
        return priKey;
    }
}