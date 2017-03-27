package com.denghb.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ppd on 2017/3/27.
 */
public class JsonUtils {

    public static String toJson(String level,String content,Throwable t){

        // appId
        // dateTime
        // className
        // classPath
        // content
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"dateTime\":");
        sb.append("\"");
        sb.append(now());
        sb.append("\",");
        sb.append("\"className\":");


        sb.append("}");

        return null;
    }

    private static String now(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");
        String ctime = formatter.format(new Date());
        return ctime;
    }

    private static String throw2Str(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}
