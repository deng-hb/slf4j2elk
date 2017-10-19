package com.denghb.slf4j2elk;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by denghb on 2016/10/12.
 *
 * @author denghb
 */
public class Slf4j2elkLoggerFactory implements ILoggerFactory {

    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    public Slf4j2elkLoggerFactory() {

    }

    /**
     * Return an appropriate {@link Slf4j2elkLogger} instance by name.
     */
    public Logger getLogger(String name) {
        Logger log = loggerMap.get(name);
        if (log != null) {
            return log;
        } else {
            Logger newInstance = new Slf4j2elkLogger(name);
            Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }

    /**
     * Clear the internal logger cache.
     * <p>
     * This method is intended to be called by classes (in the same package) for
     * testing purposes. This method is internal. It can be modified, renamed or
     * removed at any time without notice.
     * <p>
     * You are strongly discouraged from calling this method in production code.
     */
    void reset() {
        loggerMap.clear();
    }
}
