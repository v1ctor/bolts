package ru.yandex.bolts.collection;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;


public class TryTest {

    @Test
    public void cons() {
        Assert.assertTrue(Try.cons(() -> "value").isSuccess());

        Assert.assertTrue(Try.cons(() -> {
            throw new RuntimeException("fail");
        }).isFailure());
    }

    @Test
    public void getOrElse() {
        Assert.assertEquals("value", Try.success("value").getOrElse("else"));

        Assert.assertEquals("else", Try.<String>failure(new RuntimeException("fail")).getOrElse("else"));
    }

    @Test
    public void orElse() {
        Try<String> fallback = Try.success("else");
        Assert.assertEquals("value", Try.success("value").orElse(fallback).get());

        Assert.assertEquals("else", Try.<String>failure(new RuntimeException("fail")).orElse(fallback).get());
    }

    @Test
    public void toOption() {
        Assert.assertEquals(Option.some("value"), Try.success("value").toOption());

        Assert.assertEquals(Option.none(), Try.failure(new RuntimeException("fail")).toOption());
    }

    @Test
    public void getOrFail() {
        Assert.assertEquals("value", Try.success("value").getOrFail());

        RuntimeException exception = new RuntimeException("fail");
        Assert.assertEquals(exception, Try.cons(() -> Try.failure(exception).getOrFail()).failed().get());
    }

    @Test
    public void get() {
        Assert.assertEquals("value", Try.success("value").get());

        Assert.assertTrue(Try.cons(() ->
                Try.failure(new RuntimeException("fail")).get()).failed().get() instanceof UnsupportedOperationException);
    }

    @Test
    public void forEach() {
        ListF<String> values = Cf.arrayList();
        Try.success("value").forEach(values::add);
        Assert.assertEquals(Cf.list("value"), values);

        ListF<String> fails = Cf.arrayList();
        Try.<String>failure(new RuntimeException("fail")).forEach(fails::add);
        Assert.assertTrue(fails.isEmpty());
    }

    @Test
    public void flatMap() {
        Assert.assertTrue(Try.success("42").flatMap(value -> Try.cons(() -> Integer.parseInt(value))).get() == 42);

        RuntimeException exception = new RuntimeException("fail");
        Assert.assertEquals(exception, Try.<String>failure(exception)
                .flatMap(value -> Try.cons(() -> Integer.parseInt(value))).failed().get());

        Assert.assertTrue(Try.success("value")
                .flatMap(value -> Try.cons(() -> Integer.parseInt(value))).failed().get() instanceof NumberFormatException);
    }

    @Test
    public void map() {
        Assert.assertTrue(Try.success("42").map(Integer::parseInt).get() == 42);

        RuntimeException exception = new RuntimeException("fail");
        Assert.assertEquals(exception, Try.<String>failure(exception).map(Integer::parseInt).failed().get());

        Assert.assertTrue(Try.success("value").map(Integer::parseInt).failed().get() instanceof NumberFormatException);
    }

    @Test
    public void filter() {
        Assert.assertEquals("42", Try.success("42").filter(value -> true).get());

        RuntimeException exception = new RuntimeException("fail");
        Assert.assertEquals(exception, Try.<String>failure(exception).filter(value -> true).failed().get());

        Assert.assertTrue(Try.success("42").filter(value -> false).failed().get() instanceof NoSuchElementException);
    }

    @Test
    public void recoverWith() {
        Assert.assertEquals("42", Try.success("42").recoverWith(exception -> Try.success("else")).get());

        Assert.assertEquals("else", Try.<String>failure(new RuntimeException("fail"))
                .recoverWith(exception -> Try.success("else")).get());
    }

    @Test
    public void recover() {
        Assert.assertEquals("42", Try.success("42").recover(exception -> "else").get());

        Assert.assertEquals("else", Try.<String>failure(new RuntimeException("fail"))
                .recover(exception -> "else").get());

        RuntimeException fail = new RuntimeException("fail");
        Assert.assertEquals(fail, Try.failure(new RuntimeException("fail")).recover(exception -> {
            throw fail;
        }).failed().get());
    }

    @Test
    public void flatten() {
        Try<String> value = Try.success("value");
        Assert.assertEquals(value, Try.success(value).<String>flatten());

        Assert.assertTrue(Try.failure(new RuntimeException("fail")).flatten().isFailure());

        Assert.assertTrue(Try.success("value").flatten().failed().get() instanceof ClassCastException);
    }

    @Test
    public void transform() {
        Try<String> fallback = Try.success("else");
        Assert.assertEquals(fallback, Try.success("42").transform(value -> fallback, exception -> fallback));

        Assert.assertEquals(fallback, Try.failure(new RuntimeException("fail"))
                .transform(value -> fallback, exception -> fallback));

        RuntimeException fail = new RuntimeException("fail");
        Assert.assertEquals(fail, Try.failure(new RuntimeException("fail"))
                .transform(value -> { throw fail; }, exception -> { throw fail; }).failed().get());
    }

}
