package com.github.fallback.util;

/**
 * Generically indicates that something wasn't found (ie, an entity).
 * This is an application exception, not a runtime exception, so it
 * doesn't automatically rollback a transaction when thrown across
 * a boundry.
 */
@SuppressWarnings("serial")
public class NotFoundException extends Exception {
    /** */
    public NotFoundException(String message) {
        super(message);
    }

    /** */
    public NotFoundException(Throwable cause) {
        super(cause);
    }

    /** */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
