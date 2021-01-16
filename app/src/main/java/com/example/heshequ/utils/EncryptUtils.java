package com.example.heshequ.utils;

import java.security.MessageDigest;

/**
 * Created by Dengdongqi on 2018/8/10.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public final class EncryptUtils {

    public static String encryptMD5ToString(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr.toUpperCase();
    }
}
