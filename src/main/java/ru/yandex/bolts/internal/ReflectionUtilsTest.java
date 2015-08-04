package ru.yandex.bolts.internal;

import org.junit.Assert;
import org.junit.Test;


public class ReflectionUtilsTest {

    @Test
    public void getSimpleName() {
        Assert.assertEquals("File", ReflectionUtils.getSimpleName("java.io.File"));
    }

} //~
