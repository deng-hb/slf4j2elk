package com.denghb.utils;

/**
 * Created by denghb on 2017/3/26.
 */
public class StringUtils {
    public static boolean isBlank(final String string) {
        if (null != string && 0 < string.trim().length()) {
            return false;
        }
        return true;
    }

    public static boolean isBotBlank(final String string) {

        return !isBlank(string);
    }
}
