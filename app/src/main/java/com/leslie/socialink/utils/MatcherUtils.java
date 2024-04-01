package com.leslie.socialink.utils;

import com.blankj.utilcode.util.RegexUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MatcherUtils {
    public static boolean isPhone(String phone) {
        return RegexUtils.isMobileSimple(phone);
    }

    public static boolean isEmail(String emial) {
        Pattern p = Pattern.compile("\\A[^@\\s]+@[^@\\s]+\\z");
        Matcher m = p.matcher(emial);
        return m.matches();
    }

    public static boolean isRealName(String realName) {
        Pattern p = Pattern.compile("^([\\u4E00-\\u9FA5]{2,}+|[a-zA-Z]+)$");
        Matcher m = p.matcher(realName);
        return m.matches();
    }

    public static boolean isPwd(String pwd) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    public static boolean isNn(String nn) {
        if (nn.length() > 7 || nn.length() == 0) {
            return false;
        }

        return true;
    }
}
