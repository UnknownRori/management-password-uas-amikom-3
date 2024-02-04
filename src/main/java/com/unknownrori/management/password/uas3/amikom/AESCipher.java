/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unknownrori.management.password.uas3.amikom;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author UnknownRori
 */
public final class AESCipher {
    static protected final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    static protected final String ALGORITHM_GET_KEY = "AES";
    static protected final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private static SecretKey getKeyFromPassword(String password, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance(AESCipher.SECRET_KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
            .getEncoded(), ALGORITHM_GET_KEY);

        return secret;
    }
    
    public static byte[] generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    
    public static byte[] Encrypt(byte[] payload, String password, byte[] salt, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException,
            InvalidKeySpecException
    {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey key = getKeyFromPassword(password, salt);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] cipherText = cipher.doFinal(payload);
        return cipherText;
    }
    
    public static byte[] Decrypt(byte[] payload, String password, byte[] salt, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException,
            InvalidKeySpecException
    {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey key = getKeyFromPassword(password, salt);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] plainText = cipher.doFinal(payload);
        return plainText;
    }
}
