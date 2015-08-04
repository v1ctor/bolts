package ru.yandex.bolts.internal;


@SuppressWarnings("serial")
public class ReflectionException extends RuntimeException {

    public ReflectionException() {
        super();
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

} //~
