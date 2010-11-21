package ru.yandex.bolts.internal;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("serial")
public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
        super();
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(Throwable cause) {
        super(cause);
    }

} //~
