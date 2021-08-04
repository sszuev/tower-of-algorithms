package com.gitlab.sszuev.squares;

/**
 * Created by @ssz on 01.08.2021.
 */
public class ExitException extends RuntimeException {
    private final int code;

    public ExitException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
