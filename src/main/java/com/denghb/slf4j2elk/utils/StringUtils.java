package com.denghb.slf4j2elk.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by denghb on 2017/3/26.
 */
public class StringUtils {

    /**
     *
     * @param string
     * @return
     */
    public static boolean isBlank(final String string) {
        if (null != string && 0 < string.trim().length()) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param string
     * @return
     */
    public static boolean isNotBlank(final String string) {

        return !isBlank(string);
    }


    /**
     * 异常对象转换字符串
     *
     * @param t
     * @return
     */
    public static String throw2Str(final Throwable t) {
        if (null == t) {
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
