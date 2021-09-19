package com.gitlab.sszuev.tasks.sorting.external;

import java.io.PrintStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Created by @ssz on 19.09.2021.
 */
public final class Logger {
    private static final DateTimeFormatter DF = new DateTimeFormatterBuilder().appendInstant(3).toFormatter();
    private static final Logger LOGGER = new Logger();

    private static PrintStream out;

    private Logger() {
        // nothing
    }

    static void setOut(PrintStream out) {
        Logger.out = out;
    }

    public static Logger getInstance(PrintStream out) {
        setOut(out);
        return LOGGER;
    }

    public static Logger getInstance() {
        return LOGGER;
    }

    public void log(Object msg, Object... args) {
        if (out == null) {
            return;
        }
        out.println("[" + DF.format(Instant.now()) + "] -- " + String.format(String.valueOf(msg), args));
    }
}
