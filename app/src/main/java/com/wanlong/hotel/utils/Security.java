package com.wanlong.hotel.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by luo on 2018/1/24.
 */

public class Security {
    public static String encrypt(String input, String key) {
        if (input == null || input.equals("")) {
            return "";
        } else {
            byte[] crypted = null;
            try {
                SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, skey);
                crypted = cipher.doFinal(input.getBytes());
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return new String(Base64.encode(crypted, 0));
        }

    }

    public static String decrypt(String input, String key) {
        if (input == null || input.equals("")) {
            return "";
        } else {
            byte[] output = null;
            try {
                SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skey);
                output = cipher.doFinal(Base64.decode(input, 0));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return new String(output);
        }

    }

    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }
}
