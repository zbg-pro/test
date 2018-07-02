package com.zl.iolearn.insanecoder.test;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * Created by hl on 2018/6/28.
 */
public class AESDemo {
    private static String src = "TestAES+=";

    public static void jdkAES (){
        try {


            //生成Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            //keyGenerator.init(128, new SecureRandom("seedseedseed".getBytes()));
            //使用上面这种初始化方法可以特定种子来生成密钥，这样加密后的密文是唯一固定的。
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //Key转换
            Key key = new SecretKeySpec(keyBytes, "AES");

            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeResult = cipher.doFinal(AESDemo.src.getBytes());
            System.out.println("AESencode : " + Hex.encodeHexString(encodeResult) );

            //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeResult = cipher.doFinal(encodeResult);
            System.out.println("AESdecode : " + new String (decodeResult));


        } catch (NoSuchAlgorithmException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }


    public static void bcAES (){
        try {

            //使用BouncyCastle 的DES加密
            Security.addProvider(new BouncyCastleProvider());

            //生成Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES","BC");
            keyGenerator.getProvider();
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //Key转换
            Key key = new SecretKeySpec(keyBytes, "AES");

            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeResult = cipher.doFinal(AESDemo.src.getBytes());
            System.out.println("AESencode : " + Hex.encodeHexString(encodeResult) );

            //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeResult = cipher.doFinal(encodeResult);
            System.out.println("AESdecode : " + new String (decodeResult));




        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        jdkAES();
        bcAES();

    }

}
