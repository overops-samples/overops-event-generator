package com.overops.examples.error;

public class UncaughtException extends RuntimeException {
	private static final long serialVersionUID = 6415803589972729150L;

	public UncaughtException() {
    }

    public UncaughtException(String message) {
        super(message);
    }

    public UncaughtException(String message, Throwable cause) {
        super(message, cause);
    }

    public UncaughtException(Throwable cause) {
        super(cause);
    }

    public UncaughtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
