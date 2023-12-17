package com.stupidcoderx.modding.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLog {
    private static final Logger SERVER = LogManager.getFormatterLogger(Mod.id() + "/server");
    private static final Logger CLIENT = LogManager.getFormatterLogger(Mod.id() + "/client");

    private ModLog() {
    }

    private static Logger getLogger() {
        return Mod.isEnvClient ? CLIENT : SERVER;
    }

    public static void log(Level level, String message, Object... params) {
        final String formattedMessage = String.format(message, params);
        final Logger logger = getLogger();

        logger.log(level, formattedMessage);
    }

    public static void info(String format, Object... params) {
        log(Level.INFO, format, params);
    }

    public static void warn(String format, Object... params) {
        log(Level.WARN, format, params);
    }

    public static void error(String format, Object... params) {
        log(Level.ERROR, format, params);
    }

    public static void debug(String format, Object... data) {
        log(Level.DEBUG, format, data);
    }
}
