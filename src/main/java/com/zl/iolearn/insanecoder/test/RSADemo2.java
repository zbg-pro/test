package com.zl.iolearn.insanecoder.test;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * Created by hl on 2018/6/28.
 */
public class RSADemo2 {

    /**
     *生成私钥  公钥
     */
    public static void geration(){
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            FileOutputStream fos = new FileOutputStream("/Users/hl/pu.txt");
            fos.write(publicKeyBytes);
            fos.close();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            fos = new FileOutputStream("/Users/hl/pv.txt");
            fos.write(privateKeyBytes);
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 获取私钥
     * @param filename
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename)throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec =new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * 获取公钥
     * @param filename
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static void main(String[] args) {

        geration();

        String input = "!!!hello world!!!";
        RSAPublicKey pubKey;
        RSAPrivateKey privKey;
        byte[] cipherText;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            pubKey = (RSAPublicKey) getPublicKey("/Users/hl/pu.txt");
            privKey = (RSAPrivateKey) getPrivateKey("/Users/hl/pv.txt");

            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            cipherText = cipher.doFinal(input.getBytes());
            //加密后的东西
            System.out.println("cipher: " + new String(cipherText));
            //开始解密
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainText = cipher.doFinal(cipherText);
            //System.out.println("publickey: " + Base64.getEncoder().encode(cipherText));
            System.out.println("plain : " + new String(plainText));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
