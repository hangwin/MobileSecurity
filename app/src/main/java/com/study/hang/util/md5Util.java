package com.study.hang.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hang on 16/3/20.
 * md5加密工具
 */
public class md5Util {
    public static String encrypt(String str) {
        try {
            MessageDigest md5=MessageDigest.getInstance("md5");
            byte[] bytes= md5.digest(str.getBytes());
            StringBuilder builder=new StringBuilder();
            for (byte b:bytes) {
                int temp=b&0xff;
                String res=Integer.toHexString(temp);
                if(res.length()<2) {
                    builder.append("0");
                }
                builder.append(res);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
