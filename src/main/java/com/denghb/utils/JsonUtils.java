package com.denghb.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ppd on 2017/3/27.
 */
public class JsonUtils {

    public static String toJson(final String appId, final String level, final String date, final String name, final String content, final Throwable t) {

        // appId
        // dateTime
        // className
        // content
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"appId\":\"");
        sb.append(appId);

        sb.append("\",\"level\":\"");
        sb.append(level);

        sb.append("\",\"dateTime\":\"");
        sb.append(date);

        sb.append("\",\"className\":\"");
        sb.append(name);

        sb.append("\",\"content\":\"");
        sb.append(content);

        sb.append("\",\"throwable\":\"");
        sb.append(safeStr(throw2Str(t)));

        sb.append("\"}");

        return sb.toString();
    }

    private static String safeStr(String str) {
        if (null == str) {
            return null;
        }
        str = str.replaceAll("\n\t", "#L_B#");
        str = str.replaceAll("\n", "#L_B#");
        return str;
    }

    private static String throw2Str(final Throwable t) {
        if (null == t) {
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
