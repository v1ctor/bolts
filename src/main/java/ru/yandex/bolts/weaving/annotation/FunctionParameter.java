package ru.yandex.bolts.weaving.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameter marked with this annotation is a function.
 *
 * @author Stepan Koltsov
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PARAMETER)
public @interface FunctionParameter {

} //~
