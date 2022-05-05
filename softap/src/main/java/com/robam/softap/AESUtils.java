package com.robam.softap;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * @Title: AESUtils.java
 * @Package com.fendo.MD5
 * @Description: TODO
 * @author fendo
 * @date 2017年9月11日 下午5:48:17
 * @version V1.0
 */
public class AESUtils {
    public static final String CipherMode = "AES/CBC/PKCS7Padding";


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] AESEncode(byte[] content, byte[] rawKey) {
        try {
            SecretKey key = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key, createIvParameterSpec());

            byte[] byte_AES = cipher.doFinal(content);
            return byte_AES;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        //如果有错就返加null
        return null;
    }

    private static IvParameterSpec createIvParameterSpec() {
        byte[] data = new byte[16];
        for (int i = 0; i < data.length; i++){
            data[i] = 0;
        }
        return new IvParameterSpec(data);
    }

    public static String AESDecode(byte[] content, byte[] rawKey) {
        try {
            SecretKey key = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key , createIvParameterSpec());
            byte[] byte_decode = cipher.doFinal(content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        //如果有错就返加null
        return null;
    }
}