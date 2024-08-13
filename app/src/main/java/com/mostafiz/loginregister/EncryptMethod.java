package com.mostafiz.loginregister;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptMethod {

    public static String KEY="";

    public static String encrypData(String text) throws  Exception{
        String plaintext=text;
        String password="qNt)9##Fw#u6&)qa";
        byte[] plaintextBytes=plaintext.getBytes("UTF-8");
        byte[] passwordBytes=password.getBytes("UTF-8");

        SecretKey secretKey=new SecretKeySpec(passwordBytes,"AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] secureBytes=cipher.doFinal(plaintextBytes);

        String encodeString= Base64.encodeToString(secureBytes, Base64.DEFAULT);

        return encodeString;
    }
}
