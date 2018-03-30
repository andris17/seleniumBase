package utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Util class managing the log4j logger instance.
 *
 * @author Andras Fuge
 */
public class Log4jUtils {
    public Log4jUtils() {
    }

    public static void setRootLevel(String level) {
        Configurator.setRootLevel(mapLevel(level));
    }

    public static void setLevel(String className, String level) {
        Configurator.setLevel(className, mapLevel(level));
    }

    private static Level mapLevel(String levelName) {
        Level level = null;

        if (levelName != null) {
            switch (levelName) {
                case "ALL":
                    level = Level.ALL;
                    break;
                case "CRITICAL":
                    level = Level.DEBUG;
                    break;
                case "DEBUG":
                    level = Level.DEBUG;
                    break;
                case "ERROR":
                    level = Level.ERROR;
                    break;
                case "FATAL":
                    level = Level.FATAL;
                    break;
                case "INFO":
                    level = Level.INFO;
                    break;
                case "OFF":
                    level = Level.OFF;
                    break;
                case "TRACE":
                    level = Level.TRACE;
                    break;
                case "WARN":
                    level = Level.WARN;
                    break;
                default:
                    level = Level.INFO;
            }
        }

        return level;
    }
}
