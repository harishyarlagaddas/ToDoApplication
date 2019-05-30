package com.interview.todo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@Component("SecurityWrapper")
public class SecurityWrapper {

    @Autowired
    private static Environment env;

    private SecretKeySpec mSecretKey = null;

    private Cipher mCipher = null;
    private Logger mLogger = Logger.getLogger("SecurityWrapper");

    @Autowired
    public SecurityWrapper(@Value("${aes.key}") String key) {
        try {
            mLogger.log(Level.INFO, "Key: "+key);
            setKey(key);
            mCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
            mLogger.log(Level.SEVERE, "Exception while getting the cipher");
            e.printStackTrace();
        }
    }

    public void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            byte[] key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            mSecretKey = new SecretKeySpec(key, "AES");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String strToEncrypt)
    {
        try
        {
            mCipher.init(Cipher.ENCRYPT_MODE, mSecretKey);
            return Base64.getEncoder().encodeToString(mCipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            mLogger.log(Level.SEVERE, "Exception while encrypting..");
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String strToDecrypt)
    {
        try
        {
            mCipher.init(Cipher.DECRYPT_MODE, mSecretKey);
            return new String(mCipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            mLogger.log(Level.SEVERE, "Exception while decrypting..");
            e.printStackTrace();
        }
        return null;
    }
}