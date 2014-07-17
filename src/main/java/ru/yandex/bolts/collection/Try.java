package ru.yandex.bolts.collection;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;

/**
 * Port of scala <a href="http://www.scala-lang.org/files/archive/nightly/docs/library/index.html#scala.util.Try">Try</a>.
 *
 * @author Anton Bobukh <abobukh@yandex-team.ru>
 */
public interface Try<T> {

    static <T> Try<T> cons(Callable<T> func) {
        try {
            return Try.success(func.call());
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Try<T> failure(Exception error) {
        return new Failure<>(error);
    }

    static Try<Void> success() {
        return Try.success(null);
    }

    default T getOrElse(T value) {
        return isSuccess() ? get() : value;
    }

    default Try<T> orElse(Try<T> value) {
        try {
            return isSuccess() ? this : value;
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    default Option<T> toOption() {
        return Option.when(isSuccess(), this::get);
    }

    @SuppressWarnings("unchecked")
    default <U> Try<U> cast() {
        return (Try<U>) this;
    }

    boolean isFailure();

    boolean isSuccess();

    T getOrFail();

    T get();

    void forEach(Function1V<T> func);

    <U> Try<U> flatMap(Function<T, Try<U>> func);

    <U> Try<U> map(Function<T, U> func);

    Try<T> filter(Function1B<T> predicate);

    Try<T> recoverWith(Function<Exception, Try<T>> func);

    Try<T> recover(Function<Exception, T> func);

    <U> Try<U> flatten();

    Try<Exception> failed();

    void fail();

    <U> Try<U> transform(Function<T, Try<U>> mapper, Function<Exception, Try<U>> func);

    public static final class Success<T> implements Try<T> {

        private final T value;

        private Success(T value) {
            this.value = value;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public T getOrFail() {
            return value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public void forEach(Function1V<T> func) {
            func.apply(value);
        }

        @Override
        public <U> Try<U> flatMap(Function<T, Try<U>> func) {
            try {
                return func.apply(value);
            } catch (Exception e) {
                return Try.failure(e);
            }
        }

        @Override
        public <U> Try<U> map(Function<T, U> func) {
            return Try.cons(() -> func.apply(value));
        }

        @Override
        public Try<T> filter(Function1B<T> predicate) {
            try {
                if (predicate.apply(value)) {
                    return this;
                }
                return Try.failure(new NoSuchElementException("Predicate does not hold for " + value));
            } catch (Exception e) {
                return Try.failure(e);
            }
        }

        @Override
        public Try<T> recoverWith(Function<Exception, Try<T>> func) {
            return this;
        }

        @Override
        public Try<T> recover(Function<Exception, T> func) {
            return this;
        }

        @Override
        public <U> Try<U> flatten() {
            return this.<Try<U>>cast().get();
        }

        @Override
        public Try<Exception> failed() {
            return Try.failure(new UnsupportedOperationException("Success.failed"));
        }

        @Override
        public void fail() { }

        @Override
        public <U> Try<U> transform(Function<T, Try<U>> mapper, Function<Exception, Try<U>> func) {
            return flatMap(mapper);
        }

        @Override
        public String toString() {
            return "Success{" + value + "}";
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }

            Success success = (Success) other;
            return (value == success.value) || (value != null && value.equals(success.value));
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

    }

    public static final class Failure<T> implements Try<T> {

        private final Exception error;

        private Failure(Exception error) {
            this.error = error;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public T getOrFail() {
            throw new RuntimeException(error);
        }

        @Override
        public T get() {
            throw new UnsupportedOperationException("Failure.get");
        }

        @Override
        public void forEach(Function1V<T> func) { }

        @Override
        public <U> Try<U> flatMap(Function<T, Try<U>> func) {
            return cast();
        }

        @Override
        public <U> Try<U> map(Function<T, U> func) {
            return cast();
        }

        @Override
        public Try<T> filter(Function1B<T> predicate) {
            return this;
        }

        @Override
        public Try<T> recoverWith(Function<Exception, Try<T>> func) {
            try {
                return func.apply(error);
            } catch (Exception e) {
                return Try.failure(e);
            }
        }

        @Override
        public Try<T> recover(Function<Exception, T> func) {
            return Try.cons(() -> func.apply(error));
        }

        @Override
        public <U> Try<U> flatten() {
            return cast();
        }

        @Override
        public Try<Exception> failed() {
            return Try.success(error);
        }

        @Override
        public void fail() {
            throw new RuntimeException(error);
        }

        @Override
        public <U> Try<U> transform(Function<T, Try<U>> mapper, Function<Exception, Try<U>> func) {
            try {
                return func.apply(error);
            } catch (Exception e) {
                return Try.failure(e);
            }
        }

        @Override
        public String toString() {
            return "Failure{" + error + "}";
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }

            Failure failure = (Failure) other;
            return (error == failure.error) || (error != null && error.equals(failure.error));
        }

        @Override
        public int hashCode() {
            return error != null ? error.hashCode() : 0;
        }

    }

}

