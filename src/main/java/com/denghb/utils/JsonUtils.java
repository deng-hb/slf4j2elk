package com.denghb.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ppd on 2017/3/27.
 */
public class JsonUtils {

    public static String toJson(String appId, String level, String date, String name, String content, Throwable t) {

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
        sb.append(throw2Str(t));

        sb.append("\"}");

        return sb.toString();
    }

    private static String throw2Str(Throwable t) {
        if (null == t) {
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
