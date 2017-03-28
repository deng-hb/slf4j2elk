package com.denghb.slf4j2elk.domain;

import com.denghb.slf4j2elk.utils.StringUtils;

/**
 * Created by denghb on 2017/3/28.
 */
public class LoggerObject {

    private String appId;
    private String level;
    private String dateTime;
    private String className;
    private String content;
    private Throwable throwable;

    public LoggerObject(String appId, String level, String dateTime, String className, String content, Throwable throwable) {
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
            buf.append(StringUtils.throw2Str(throwable));
        }
        return buf.toString();
    }
}
