package com.denghb.slf4j2elk;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.slf4j.helpers.Util;

/**
 * This class holds configuration values for {@link Slf4j2elkLogger}. The
 * values are computed at runtime. See {@link Slf4j2elkLogger} documentation for
 * more information.
 *
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Scott Sanders
 * @author Rod Waldhoff
 * @author Robert Burrell Donkin
 * @author C&eacute;drik LIME
 *
 * @since 1.7.25
 */
public class Slf4j2elkLoggerConfiguration {

    private static final String CONFIGURATION_FILE = "slf4j2elk.properties";

    static int DEFAULT_LOG_LEVEL_DEFAULT = Slf4j2elkLogger.LOG_LEVEL_INFO;
    int defaultLogLevel = DEFAULT_LOG_LEVEL_DEFAULT;

    private static final boolean SHOW_DATE_TIME_DEFAULT = false;
    boolean showDateTime = SHOW_DATE_TIME_DEFAULT;

    private static final String DATE_TIME_FORMAT_STR_DEFAULT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static String dateTimeFormatStr = DATE_TIME_FORMAT_STR_DEFAULT;

    DateFormat dateFormatter = null;

    private static final boolean SHOW_THREAD_NAME_DEFAULT = true;
    boolean showThreadName = SHOW_THREAD_NAME_DEFAULT;

    final static boolean SHOW_LOG_NAME_DEFAULT = true;
    boolean showLogName = SHOW_LOG_NAME_DEFAULT;

    private static final boolean SHOW_SHORT_LOG_NAME_DEFAULT = false;
    boolean showShortLogName = SHOW_SHORT_LOG_NAME_DEFAULT;

    private static final boolean LEVEL_IN_BRACKETS_DEFAULT = false;
    boolean levelInBrackets = LEVEL_IN_BRACKETS_DEFAULT;

    private static String LOG_FILE_DEFAULT = "com/denghb/slf4j2elk";
    private String logFile = LOG_FILE_DEFAULT;

    private static final boolean CACHE_OUTPUT_STREAM_DEFAULT = false;
    private boolean cacheOutputStream = CACHE_OUTPUT_STREAM_DEFAULT;

    private static final String WARN_LEVELS_STRING_DEFAULT = "WARN";
    String warnLevelString = WARN_LEVELS_STRING_DEFAULT;

    private final Properties properties = new Properties();

    String server = null;
    String file = null;
    String id = "10000";


    void init() {
        loadProperties();

        String defaultLogLevelString = getStringProperty(Slf4j2elkLogger.LOG_LEVEL_KEY, null);
        if (defaultLogLevelString != null)
            defaultLogLevel = stringToLevel(defaultLogLevelString);

        // @denghb 初始化日志配置信息
        id = getStringProperty(Slf4j2elkLogger.LOG_ID_KEY, id);
        server = getStringProperty(Slf4j2elkLogger.LOG_SERVER_KEY, server);
        logFile = getStringProperty(Slf4j2elkLogger.LOG_FILE_KEY, logFile);

//        showLogName = getBooleanProperty(Slf4j2elkLogger.SHOW_LOG_NAME_KEY, Slf4j2elkLoggerConfiguration.SHOW_LOG_NAME_DEFAULT);
//        showShortLogName = getBooleanProperty(Slf4j2elkLogger.SHOW_SHORT_LOG_NAME_KEY, SHOW_SHORT_LOG_NAME_DEFAULT);
//        showDateTime = getBooleanProperty(Slf4j2elkLogger.SHOW_DATE_TIME_KEY, SHOW_DATE_TIME_DEFAULT);
//        showThreadName = getBooleanProperty(Slf4j2elkLogger.SHOW_THREAD_NAME_KEY, SHOW_THREAD_NAME_DEFAULT);
//        dateTimeFormatStr = getStringProperty(Slf4j2elkLogger.DATE_TIME_FORMAT_KEY, DATE_TIME_FORMAT_STR_DEFAULT);
//        levelInBrackets = getBooleanProperty(Slf4j2elkLogger.LEVEL_IN_BRACKETS_KEY, LEVEL_IN_BRACKETS_DEFAULT);
//        warnLevelString = getStringProperty(Slf4j2elkLogger.WARN_LEVEL_STRING_KEY, WARN_LEVELS_STRING_DEFAULT);


//        cacheOutputStream = getBooleanProperty(Slf4j2elkLogger.CACHE_OUTPUT_STREAM_STRING_KEY, CACHE_OUTPUT_STREAM_DEFAULT);

        if (dateTimeFormatStr != null) {
            try {
                dateFormatter = new SimpleDateFormat(dateTimeFormatStr);
            } catch (IllegalArgumentException e) {
                Util.report("Bad date format in " + CONFIGURATION_FILE + "; will output relative time", e);
            }
        }
    }

    private void loadProperties() {
        // Add props from the resource simplelogger.properties
        InputStream in = AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
            public InputStream run() {
                ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
                if (threadCL != null) {
                    return threadCL.getResourceAsStream(CONFIGURATION_FILE);
                } else {
                    return ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE);
                }
            }
        });
        if (null != in) {
            try {
                properties.load(in);
            } catch (java.io.IOException e) {
                // ignored
            } finally {
                try {
                    in.close();
                } catch (java.io.IOException e) {
                    // ignored
                }
            }
        }
    }

    String getStringProperty(String name, String defaultValue) {
        String prop = getStringProperty(name);
        return (prop == null) ? defaultValue : prop;
    }

    boolean getBooleanProperty(String name, boolean defaultValue) {
        String prop = getStringProperty(name);
        return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
            ; // Ignore
        }
        return (prop == null) ? properties.getProperty(name) : prop;
    }

    static int stringToLevel(String levelStr) {
        if ("trace".equalsIgnoreCase(levelStr)) {
            return Slf4j2elkLogger.LOG_LEVEL_TRACE;
        } else if ("debug".equalsIgnoreCase(levelStr)) {
            return Slf4j2elkLogger.LOG_LEVEL_DEBUG;
        } else if ("info".equalsIgnoreCase(levelStr)) {
            return Slf4j2elkLogger.LOG_LEVEL_INFO;
        } else if ("warn".equalsIgnoreCase(levelStr)) {
            return Slf4j2elkLogger.LOG_LEVEL_WARN;
        } else if ("error".equalsIgnoreCase(levelStr)) {
            return Slf4j2elkLogger.LOG_LEVEL_ERROR;
        } else if ("off".equalsIgnoreCase(levelStr)) {
            return Slf4j2elkLogger.LOG_LEVEL_OFF;
        }
        // assume INFO by default
        return Slf4j2elkLogger.LOG_LEVEL_INFO;
    }


}
