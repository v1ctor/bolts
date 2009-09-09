package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.function.Function;

/**
 * Functions on {@link Object}
 * 
 * @author Stepan Koltsov
 */
public class ObjectF {
	
	public static Function<Object, Integer> hashCodeF() {
		return new Function<Object, Integer>() {
			public Integer apply(Object a) {
				return a != null ? a.hashCode() : 0;
			}
		};
	}
	
	public static Function<Object, String> toStringF() {
		return new Function<Object, String>() {
			public String apply(Object a) {
				return a != null ? a.toString() : "null";
			}

			@Override
			public String toString() {
				return "toString";
			}
		};
	}
	
} //~
