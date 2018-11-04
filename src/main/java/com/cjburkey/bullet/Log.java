package com.cjburkey.bullet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Log {
    
    public static final Logger logger = LogManager.getLogger("bullet-lang");
    
    public static void debug(Object msg, Object... data) {
        logger.debug(sanitize(msg), data);
    }
    
    public static void info(Object msg, Object... data) {
        logger.info(sanitize(msg), data);
    }
    
    public static void warn(Object msg, Object... data) {
        logger.warn(sanitize(msg), data);
    }
    
    public static void error(Object msg, Object... data) {
        logger.error(sanitize(msg), data);
    }
    
    public static void exception(Throwable exception) {
        error("A fatal exception occurred");
        Throwable current = exception;
        do {
            stackTrace(current);
        } while ((current = exception.getCause()) != null);
    }
    
    private static void stackTrace(Throwable throwable) {
        error("Exception: {}", throwable.getLocalizedMessage());
        for (StackTraceElement stackTrace : throwable.getStackTrace()) {
            error("    {}", stackTrace);
        }
    }
    
    private static String sanitize(Object msg) {
        if (msg == null) {
            return "null";
        }
        String out = msg.toString();
        return out == null ? "null" : out;
    }
    
}
