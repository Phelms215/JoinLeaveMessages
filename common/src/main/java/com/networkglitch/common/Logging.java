package com.networkglitch.common;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.Arrays;

import static com.networkglitch.common.Config.ModName;

public final class Logging {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void MixinException(Exception exception) {
        error("Unexpected error occurred during a mixin attempt. See the below exception for more details");
        error(exception.getLocalizedMessage());
        Arrays.stream(exception.getSuppressed()).iterator().forEachRemaining(ex -> {
            error(ex.getLocalizedMessage());
        });
        info(Arrays.toString(exception.getStackTrace()));
    }
    public static void EnableDebug() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LOGGER.getName());
        loggerConfig.setLevel(Level.DEBUG);
        ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
    }

    public static void EnabledMessage() {
        info("Enabled");
    }


    public static void error(String message) {
        LOGGER.error("[" + ModName +"] "+ message);
    }

    public static void info(String message) {
        LOGGER.info("[" + ModName +"] "+ message);
    }

    public static void debug(String message) {
        LOGGER.debug("[" + ModName +"] "+ message);
    }
}