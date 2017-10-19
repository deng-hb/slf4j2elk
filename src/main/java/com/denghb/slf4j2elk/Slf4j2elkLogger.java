package com.denghb.slf4j2elk;

import com.denghb.slf4j2elk.domain.ElkLoggerObject;
import com.denghb.slf4j2elk.utils.ReadConfigUtils;
import com.denghb.slf4j2elk.utils.FileUtils;
import com.denghb.slf4j2elk.utils.HttpUtils;
import com.denghb.slf4j2elk.utils.StringUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by denghb on 2017/10/19.
 *
 * @author denghb.com
 */
public class Slf4j2elkLogger extends MarkerIgnoringBase {

    private static final long serialVersionUID = -632788891211436180L;

    /**
     * The current log level
     */
    protected int currentLogLevel = LocationAwareLogger.INFO_INT;

    private static String CONFIG_LEVEL;
    private static String CONFIG_ID;
    private static String CONFIG_SERVER;
    private static String CONFIG_FILE;
    private static String CONFIG_DEBUG;

    private static List<String> debugPackages;

    // 加载配置
    static {
        String SYSTEM_PREFIX = "com.denghb.slf4j2elk.";

        CONFIG_LEVEL = ReadConfigUtils.getValue(SYSTEM_PREFIX + "level");
        CONFIG_ID = ReadConfigUtils.getValue(SYSTEM_PREFIX + "id");
        CONFIG_SERVER = ReadConfigUtils.getValue(SYSTEM_PREFIX + "server");
        CONFIG_FILE = ReadConfigUtils.getValue(SYSTEM_PREFIX + "file");
        CONFIG_DEBUG = ReadConfigUtils.getValue(SYSTEM_PREFIX + "debug");

        if (StringUtils.isNotBlank(CONFIG_DEBUG)) {
            String[] strings = CONFIG_DEBUG.split(",");
            debugPackages = Arrays.asList(strings);
        }

    }


    /**
     * Package access allows only {@link Slf4j2elkLoggerFactory} to instantiate
     * Slf4j2elkLogger instances.
     */
    public Slf4j2elkLogger(String name) {
        this.name = name;

        if (StringUtils.isNotBlank(CONFIG_LEVEL)) {
            currentLogLevel = renderLevel(CONFIG_LEVEL);
        }
    }

    /**
     * denghb
     *
     * @param level   One of the LOG_LEVEL_XXX constants defining the log level
     * @param message The message itself
     * @param t       The exception whose stack trace should be logged
     */
    private void log(int level, String message, Throwable t) {


        if (!isLevelEnabled(level)) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String now = sdf.format(new Date());
        String levelStr = renderLevel(level);
        ElkLoggerObject logger = new ElkLoggerObject(CONFIG_ID, levelStr, now, name, message, t);


        // 输入Console
        String log = logger.toString();

        PrintStream targetStream = System.out;
        targetStream.println(log);
        targetStream.flush();

        // 发送http请求到ELK
        if (StringUtils.isNotBlank(CONFIG_SERVER)) {
            String body = logger.toJsonString();
            HttpUtils.send(CONFIG_SERVER, body);
        }

        // 写入文件
        if (StringUtils.isNotBlank(CONFIG_FILE)) {

            FileUtils.write(CONFIG_FILE, log);
            // 分开写错误日志文件
            if (LocationAwareLogger.ERROR_INT == level) {
                FileUtils.writeError(CONFIG_FILE, log);
            }
        }

    }

    private String renderLevel(int level) {
        switch (level) {
            case LocationAwareLogger.TRACE_INT:
                return "TRACE";
            case LocationAwareLogger.DEBUG_INT:
                return "DEBUG";
            case LocationAwareLogger.INFO_INT:
                return "INFO";
            case LocationAwareLogger.WARN_INT:
                return "WARN";
            case LocationAwareLogger.ERROR_INT:
                return "ERROR";
        }
        throw new IllegalStateException("Unrecognized level [" + level + "]");
    }


    private int renderLevel(String level) {
        if ("TRACE".equalsIgnoreCase(level)) {
            return LocationAwareLogger.TRACE_INT;
        } else if ("DEBUG".equalsIgnoreCase(level)) {
            return LocationAwareLogger.DEBUG_INT;
        } else if ("INFO".equalsIgnoreCase(level)) {
            return LocationAwareLogger.INFO_INT;
        } else if ("WARN".equalsIgnoreCase(level)) {
            return LocationAwareLogger.WARN_INT;
        } else if ("ERROR".equalsIgnoreCase(level)) {
            return LocationAwareLogger.ERROR_INT;
        } else {
            throw new IllegalStateException("Unrecognized level [" + level + "]");
        }
    }


    /**
     * For formatted messages, first substitute arguments and then log.
     *
     * @param level
     * @param format
     * @param arg1
     * @param arg2
     */
    private void formatAndLog(int level, String format, Object arg1, Object arg2) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     *
     * @param level
     * @param format
     * @param arguments a list of 3 ore more arguments
     */
    private void formatAndLog(int level, String format, Object... arguments) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * Is the given log level currently enabled?
     *
     * @param logLevel is this level enabled?
     */
    protected boolean isLevelEnabled(int logLevel) {

        // 指定包debug
        if (LocationAwareLogger.DEBUG_INT == logLevel && null != debugPackages) {
            for (String p : debugPackages) {
                if (name.startsWith(p)) {
                    return true;
                }
            }
        }

        // log level are numerically ordered so can use simple numeric
        // comparison
        return (logLevel >= currentLogLevel);
    }

    /**
     * Are {@code trace} messages currently enabled?
     */
    public boolean isTraceEnabled() {
        return isLevelEnabled(LocationAwareLogger.TRACE_INT);
    }

    /**
     * A simple implementation which logs messages of level TRACE according to
     * the format outlined above.
     */
    public void trace(String msg) {
        log(LocationAwareLogger.TRACE_INT, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * TRACE according to the format outlined above.
     */
    public void trace(String format, Object param1) {
        formatAndLog(LocationAwareLogger.TRACE_INT, format, param1, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * TRACE according to the format outlined above.
     */
    public void trace(String format, Object param1, Object param2) {
        formatAndLog(LocationAwareLogger.TRACE_INT, format, param1, param2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * TRACE according to the format outlined above.
     */
    public void trace(String format, Object... argArray) {
        formatAndLog(LocationAwareLogger.TRACE_INT, format, argArray);
    }

    /**
     * Log a message of level TRACE, including an exception.
     */
    public void trace(String msg, Throwable t) {
        log(LocationAwareLogger.TRACE_INT, msg, t);
    }

    /**
     * Are {@code debug} messages currently enabled?
     */
    public boolean isDebugEnabled() {
        return isLevelEnabled(LocationAwareLogger.DEBUG_INT);
    }

    /**
     * A simple implementation which logs messages of level DEBUG according to
     * the format outlined above.
     */
    public void debug(String msg) {
        log(LocationAwareLogger.DEBUG_INT, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * DEBUG according to the format outlined above.
     */
    public void debug(String format, Object param1) {
        formatAndLog(LocationAwareLogger.DEBUG_INT, format, param1, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * DEBUG according to the format outlined above.
     */
    public void debug(String format, Object param1, Object param2) {
        formatAndLog(LocationAwareLogger.DEBUG_INT, format, param1, param2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * DEBUG according to the format outlined above.
     */
    public void debug(String format, Object... argArray) {
        formatAndLog(LocationAwareLogger.DEBUG_INT, format, argArray);
    }

    /**
     * Log a message of level DEBUG, including an exception.
     */
    public void debug(String msg, Throwable t) {
        log(LocationAwareLogger.DEBUG_INT, msg, t);
    }

    /**
     * Are {@code info} messages currently enabled?
     */
    public boolean isInfoEnabled() {
        return isLevelEnabled(LocationAwareLogger.INFO_INT);
    }

    /**
     * A simple implementation which logs messages of level INFO according to
     * the format outlined above.
     */
    public void info(String msg) {
        log(LocationAwareLogger.INFO_INT, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * INFO according to the format outlined above.
     */
    public void info(String format, Object arg) {
        formatAndLog(LocationAwareLogger.INFO_INT, format, arg, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * INFO according to the format outlined above.
     */
    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(LocationAwareLogger.INFO_INT, format, arg1, arg2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * INFO according to the format outlined above.
     */
    public void info(String format, Object... argArray) {
        formatAndLog(LocationAwareLogger.INFO_INT, format, argArray);
    }

    /**
     * Log a message of level INFO, including an exception.
     */
    public void info(String msg, Throwable t) {
        log(LocationAwareLogger.INFO_INT, msg, t);
    }

    /**
     * Are {@code warn} messages currently enabled?
     */
    public boolean isWarnEnabled() {
        return isLevelEnabled(LocationAwareLogger.WARN_INT);
    }

    /**
     * A simple implementation which always logs messages of level WARN
     * according to the format outlined above.
     */
    public void warn(String msg) {
        log(LocationAwareLogger.WARN_INT, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * WARN according to the format outlined above.
     */
    public void warn(String format, Object arg) {
        formatAndLog(LocationAwareLogger.WARN_INT, format, arg, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * WARN according to the format outlined above.
     */
    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(LocationAwareLogger.WARN_INT, format, arg1, arg2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * WARN according to the format outlined above.
     */
    public void warn(String format, Object... argArray) {
        formatAndLog(LocationAwareLogger.WARN_INT, format, argArray);
    }

    /**
     * Log a message of level WARN, including an exception.
     */
    public void warn(String msg, Throwable t) {
        log(LocationAwareLogger.WARN_INT, msg, t);
    }

    /**
     * Are {@code error} messages currently enabled?
     */
    public boolean isErrorEnabled() {
        return isLevelEnabled(LocationAwareLogger.ERROR_INT);
    }

    /**
     * A simple implementation which always logs messages of level ERROR
     * according to the format outlined above.
     */
    public void error(String msg) {
        log(LocationAwareLogger.ERROR_INT, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * ERROR according to the format outlined above.
     */
    public void error(String format, Object arg) {
        formatAndLog(LocationAwareLogger.ERROR_INT, format, arg, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * ERROR according to the format outlined above.
     */
    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(LocationAwareLogger.ERROR_INT, format, arg1, arg2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * ERROR according to the format outlined above.
     */
    public void error(String format, Object... argArray) {
        formatAndLog(LocationAwareLogger.ERROR_INT, format, argArray);
    }

    public void error(String msg, Throwable t) {
        log(LocationAwareLogger.ERROR_INT, msg, t);
    }
/*
    public void log(LoggingEvent event) {
        int levelInt = event.getLevel().toInt();

        if (!isLevelEnabled(levelInt)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(event.getMessage(), event.getArgumentArray(), event.getThrowable());
        log(levelInt, tp.getMessage(), event.getThrowable());
    }
*/
}
