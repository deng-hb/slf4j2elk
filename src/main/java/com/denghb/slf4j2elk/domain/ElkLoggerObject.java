package com.denghb.slf4j2elk.domain;

import com.denghb.slf4j2elk.utils.StringUtils;

/**
 * Created by denghb on 2017/3/28.
 */
public class ElkLoggerObject {

    private String appId;
    private String level;
    private String dateTime;
    private String className;
    private String content;
    private Throwable throwable;

    public ElkLoggerObject(String appId, String level, String dateTime, String className, String content, Throwable throwable) {
        this.appId = appId;
        this.level = level;
        this.dateTime = dateTime;
        this.className = className;
        this.content = content;
        this.throwable = throwable;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String toString() {

        StringBuffer buf = new StringBuffer();

        buf.append(dateTime);
        buf.append(' ');

        // Append current thread name if so configured
        buf.append('[');
        buf.append(Thread.currentThread().getName());
        buf.append("] ");
        buf.append('[');

        // Append a readable representation of the log level
        buf.append(level);
        buf.append(']');
        buf.append(' ');

        // Append the name of the log instance if so configured
        buf.append(className).append(" - ");

        // Append the message
        buf.append(content);

        if (null != throwable) {
            buf.append("  ");
            buf.append(StringUtils.throw2Str(throwable));
        }
        return buf.toString();
    }

    /**
     * 返回json 字符
     *
     * @return
     */
    public String toJsonString() {

        StringBuffer sb = new StringBuffer("{");
        sb.append("\"appId\":\"");
        sb.append(this.getAppId());

        sb.append("\",\"level\":\"");
        sb.append(this.getLevel());

        sb.append("\",\"dateTime\":\"");
        sb.append(this.getDateTime());

        sb.append("\",\"className\":\"");
        sb.append(this.getClassName());

        sb.append("\",\"content\":\"");
        sb.append(this.getContent());

        sb.append("\",\"throwable\":\"");

        Throwable t = this.getThrowable();
        if (null != t) {
            String str = StringUtils.throw2Str(t);
            str = str.replaceAll("\n", "\\n");
            sb.append(str);
        }
        sb.append("\"}");

        return sb.toString();
    }
}
