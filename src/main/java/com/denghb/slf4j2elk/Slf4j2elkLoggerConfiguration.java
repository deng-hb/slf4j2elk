package com.denghb.slf4j2elk;


import java.io.InputStream;

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
 * @author Ceki G&uuml;lc&uuml;
 * @author Scott Sanders
 * @author Rod Waldhoff
 * @author Robert Burrell Donkin
 * @author C&eacute;drik LIME
 * @since 1.7.25
 */
public class Slf4j2elkLoggerConfiguration {

    private static final String CONFIGURATION_FILE = "slf4j2elk.properties";

    static int DEFAULT_LOG_LEVEL_DEFAULT = Slf4j2elkLogger.LOG_LEVEL_INFO;
    int defaultLogLevel = DEFAULT_LOG_LEVEL_DEFAULT;

    private static final String DATE_TIME_FORMAT_STR_DEFAULT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static String dateTimeFormatStr = DATE_TIME_FORMAT_STR_DEFAULT;

    DateFormat dateFormatter = null;

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
        file = getStringProperty(Slf4j2elkLogger.LOG_FILE_KEY, file);

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
            // Ignore
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
