package com.gitlab.sszuev;

import org.openjdk.jmh.Main;

import java.io.IOException;

/**
 * Created by @ssz on 28.08.2021.
 */
public class App {

    static {
        forciblyDisableJDKWarnings();
    }

    public static void main(String... args) throws IOException {
        Main.main(args);
    }

    /**
     * Workaround to suppress warnings.
     * Checked on jdk11 (11.0.9), win10x64
     *
     * @see <a href='https://stackoverflow.com/questions/46454995/'>SO</a>
     */
    private static void forciblyDisableJDKWarnings() {
        try {
            java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            sun.misc.Unsafe unsafe = (sun.misc.Unsafe) field.get(null);
            Class<?> clazz = Class.forName("jdk.internal.module.IllegalAccessLogger");
            java.lang.reflect.Field logger = clazz.getDeclaredField("logger");
            unsafe.putObjectVolatile(clazz, unsafe.staticFieldOffset(logger), null);
        } catch (Exception ignore) {
            // ignore
        }
    }

}
