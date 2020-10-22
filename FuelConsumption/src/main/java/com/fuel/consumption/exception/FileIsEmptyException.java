package com.fuel.consumption.exception;

public final class FileIsEmptyException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public FileIsEmptyException() {
        super();
    }

    public FileIsEmptyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FileIsEmptyException(final String message) {
        super(message);
    }

    public FileIsEmptyException(final Throwable cause) {
        super(cause);
    }

}
