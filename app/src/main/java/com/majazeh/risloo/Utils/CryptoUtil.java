package com.majazeh.risloo.Utils;

import android.util.Base64;
import android.util.Log;

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

public class CryptoUtil {

    private final static String CRYPTO_METHOD = "RSA";
    private final static int CRYPTO_BITS = 2048;



    public static String encrypt(String plain, String pubk)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeySpecException {

        StringBuffer stringBuffer = new StringBuffer();

            stringBuffer.append(plain.substring(0, 100));
        Log.e("encode", String.valueOf(Base64.decode(stringBuffer.toString().getBytes(),1 )));




        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");

        cipher.init(Cipher.ENCRYPT_MODE, stringToPublicKey(getMainPublicKey(pubk)));

        byte[] encryptedBytes = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    public static String decrypt(String result, String privk)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            BadPaddingException,
            IllegalBlockSizeException,
            InvalidKeySpecException,
            InvalidKeyException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, stringToPrivateKey(getMainPrivateKey(privk)));
        byte[] decryptedBytes = cipher.doFinal(Base64.decode(result, Base64.DEFAULT));
        return new String(decryptedBytes);
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
        String publickKey = "";
        if (pubKey.startsWith("-----BEGIN PUBLIC KEY-----") && pubKey.endsWith("-----END PUBLIC KEY-----")) {
            publickKey = pubKey.substring(26, pubKey.length() - 24);
        } else {
            publickKey = pubKey;
        }
        return publickKey;
    }

    private static String getMainPrivateKey(String privateKey) {
        String priKey = "";
        if (privateKey.startsWith("-----BEGIN RSA PRIVATE KEY-----") && privateKey.endsWith("-----END RSA PRIVATE KEY-----")) {
            priKey = privateKey.substring(31, privateKey.length() - 29);
        } else {
            priKey = privateKey;
        }
        return priKey;
    }
}
