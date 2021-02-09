package com.majazeh.risloo.Utils.Managers;

import android.util.Base64;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CryptoManager {

    public static String encrypt(String text, String publicKey)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            BadPaddingException,
            IllegalBlockSizeException,
            InvalidKeySpecException,
            InvalidKeyException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, stringToPublicKey(getMainPublicKey(publicKey)));

        byte[] data = text.getBytes();
        int chunkSize = 245;
        int encryptSize = (int) (Math.ceil(data.length / 245.0) * 256);
        int idx = 0;

        ByteBuffer buffer = ByteBuffer.allocate(encryptSize);
        while (idx < data.length) {
            int len = Math.min(data.length - idx, chunkSize);
            byte[] encryptChunk = cipher.doFinal(data, idx, len);

            buffer.put(encryptChunk);
            idx += len;
        }

        byte[] encryptData = buffer.array();
        return Base64.encodeToString(encryptData, Base64.DEFAULT);
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

        byte[] data = Base64.decode(result,1);
        int chunkSize = 256;
        int idx = 0;

        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        while(idx < data.length) {
            int len = Math.min(data.length-idx, chunkSize);
            byte[] decryptchunk = cipher.doFinal(data, idx, len);

            buffer.put(decryptchunk);
            idx += len;
        }

        byte[] decryptData = buffer.array();
        return new String(decryptData).replaceAll("\u0000.*", "");
    }

    private static PublicKey stringToPublicKey(String publicKey)
            throws InvalidKeySpecException,
            NoSuchAlgorithmException {

        byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    private static PrivateKey stringToPrivateKey(String privateKey)
            throws InvalidKeySpecException,
            NoSuchAlgorithmException {

        byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private static String getMainPublicKey(String publicKey) {
        String pubKey = "";
        publicKey.trim();
        if (publicKey.startsWith("-----BEGIN PUBLIC KEY-----") && publicKey.endsWith("-----END PUBLIC KEY-----")) {
            pubKey = publicKey.substring(26, publicKey.length() - 24);
        } else {
            pubKey = publicKey;
        }
        return pubKey;
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