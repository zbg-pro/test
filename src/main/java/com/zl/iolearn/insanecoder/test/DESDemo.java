package com.zl.iolearn.insanecoder.test;


import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by hl on 2018/6/28.
 */
public class DESDemo {

    private static String src = "TestDES";


    public static void jdkDES () {

        try {
            //生成密钥Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(56);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] bytesKey = secretKey.getEncoded();


            //KEY转换
            DESKeySpec deSedeKeySpec = new DESKeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            Key convertSecretKey = factory.generateSecret(deSedeKeySpec);

            //加密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
            byte[] encodeResult = cipher.doFinal(DESDemo.src.getBytes());
            System.out.println("DESEncode :" + Hex.encodeHexString(encodeResult));


            //解密
            cipher.init(Cipher.DECRYPT_MODE,convertSecretKey);
            byte[] DecodeResult = cipher.doFinal(encodeResult);
            System.out.println("DESDncode :" + new String (DecodeResult));



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }



    public static void bcDES (){
        try {


            //使用BouncyCastle 的DES加密
            Security.addProvider(new BouncyCastleProvider());


            //生成密钥Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES","BC");
            keyGenerator.init(56);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] bytesKey = secretKey.getEncoded();


            //KEY转换
            DESKeySpec deSedeKeySpec = new DESKeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            Key convertSecretKey = factory.generateSecret(deSedeKeySpec);

            //加密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
            byte[] encodeResult = cipher.doFinal(DESDemo.src.getBytes());
            System.out.println("DESEncode :" + Hex.encodeHexString(encodeResult));


            //解密
            cipher.init(Cipher.DECRYPT_MODE,convertSecretKey);
            byte[] DecodeResult = cipher.doFinal(encodeResult);
            System.out.println("DESDncode :" + new String (DecodeResult));



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DESDemo.jdkDES ();
        DESDemo.bcDES();
    }

}
