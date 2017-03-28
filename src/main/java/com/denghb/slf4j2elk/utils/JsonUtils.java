package com.denghb.slf4j2elk.utils;

import com.denghb.slf4j2elk.domain.LoggerObject;

/**
 * Created by ppd on 2017/3/27.
 */
public class JsonUtils {

    public static String toJson(LoggerObject logger) {

        StringBuffer sb = new StringBuffer("{");
        sb.append("\"appId\":\"");
        sb.append(logger.getAppId());

        sb.append("\",\"level\":\"");
        sb.append(logger.getLevel());

        sb.append("\",\"dateTime\":\"");
        sb.append(logger.getDateTime());

        sb.append("\",\"className\":\"");
        sb.append(logger.getClassName());

        sb.append("\",\"content\":\"");
        sb.append(logger.getContent());

        sb.append("\",\"throwable\":\"");

        Throwable t = logger.getThrowable();
        if (null != t) {
            String str = StringUtils.throw2Str(t);
            // TODO JSON换行问题
            str = str.replaceAll("\n\t", "</br>");
            str = str.replaceAll("\n", "</br>");
            sb.append(str);
        }
        sb.append("\"}");

        return sb.toString();
    }


}
