package co.morillas.gramgames;

import java.util.logging.*;

public class Logger {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getAnonymousLogger();

    static {
        LOG.setLevel(Level.ALL);
    }

    public static void info(String message) {
        LOG.info(message);
    }

    public static void error(String message) {
        LOG.severe(message);
    }
}
