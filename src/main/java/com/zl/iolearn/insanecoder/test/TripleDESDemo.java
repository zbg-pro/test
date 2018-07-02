package com.zl.iolearn.insanecoder.test;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;
import java.security.Security;


/**
 * Created by hl on 2018/6/28.
 */
public class TripleDESDemo {


        private static String src = "TestTripleDES";

        public static void jdkTripleDES () {

            try {
                //生成密钥Key
                KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
                keyGenerator.init(168);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] bytesKey = secretKey.getEncoded();


                //KEY转换
                DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(bytesKey);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
                Key convertSecretKey = factory.generateSecret(deSedeKeySpec);

                //加密
                Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
                byte[] encodeResult = cipher.doFinal(TripleDESDemo.src.getBytes());
                System.out.println("TripleDESEncode :" + Hex.encodeHexString(encodeResult));


                //解密
                cipher.init(Cipher.DECRYPT_MODE,convertSecretKey);
                byte[] DecodeResult = cipher.doFinal(encodeResult);
                System.out.println("TripleDESDncode :" + new String (DecodeResult));



            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }

        }




        public static void bcTripleDES () {

            try {

                Security.addProvider(new BouncyCastleProvider());
                //生成密钥Key
                KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede","BC");
                keyGenerator.getProvider();
                keyGenerator.init(168);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] bytesKey = secretKey.getEncoded();


                //KEY转换
                DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(bytesKey);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
                Key convertSecretKey = factory.generateSecret(deSedeKeySpec);

                //加密
                Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
                byte[] encodeResult = cipher.doFinal(TripleDESDemo.src.getBytes());
                System.out.println("TripleDESEncode :" + Hex.encodeHexString(encodeResult));


                //解密
                cipher.init(Cipher.DECRYPT_MODE,convertSecretKey);
                byte[] DecodeResult = cipher.doFinal(encodeResult);
                System.out.println("TripleDESDncode :" + new String (DecodeResult));



            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }

        }



        public static void main(String[] args) {
            jdkTripleDES ();
            bcTripleDES ();

        }

    }
