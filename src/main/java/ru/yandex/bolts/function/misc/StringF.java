package ru.yandex.bolts.function.misc;

import java.util.regex.Pattern;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Tuple3;
import ru.yandex.bolts.collection.Tuple4;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;

/**
 * Misc {@link String} functions.
 * 
 * @author Stepan Koltsov
 * @see CharSequenceF
 */
public class StringF {
    
    /**
     * @see CharSequenceF#notEmptyF()
     */
    public static Function1B<String> notEmptyF() {
        return CharSequenceF.notEmptyF().uncheckedCast();
    }

    /**
     * @see CharSequenceF#emptyF()
     */
    public static Function1B<String> emptyF() {
        return CharSequenceF.emptyF().uncheckedCast();
    }
    
    /**
     * @see CharSequenceF#lengthF()
     */
    public static Function<String, Integer> lengthF() {
        return CharSequenceF.lengthF().uncheckedCast();
    }
    
    /**
     * Concatenate two strings.
     */
    public static Function2<String, String, String> plusF() {
        return new Function2<String, String, String>() {
            public String apply(String a, String b) {
                return a + b;
            }
        };
    }
    
    /**
     * Join string using specified separator.
     */
    public static Function2<String, String, String> plusF(final String sep) {
        return new Function2<String, String, String>() {
            public String apply(String a, String b) {
                return a + sep + b;
            }
        };
    }
    
    public static Function<String, String> addSuffixF(String suffix) {
        return plusF().bind2(suffix);
    }
    
    public static Function<String, String> addPrefixF(String prefix) {
        return plusF().bind1(prefix);
    }
    
    public static Function2B<String, String> startsWithF() {
        return new Function2B<String, String>() {
            public boolean apply(String a, String b) {
                return a.startsWith(b);
            }
        };
    }
    
    public static Function1B<String> startsWithF(String prefix) {
        return startsWithF().bind2(prefix);
    }
    
    public static Function2B<String, String> endsWithF() {
        return new Function2B<String, String>() {
            public boolean apply(String a, String b) {
                return a.endsWith(b);
            }
        };
    }
    
    public static Function1B<String> endsWithF(String suffix) {
        return endsWithF().bind2(suffix);
    }
    
    public static Function<String, String> toLowerCaseF() {
        return new Function<String, String>() {
            public String apply(String a) {
                return a.toLowerCase();
            }
        };
    }
    
    public static Function<String, String> toUpperCaseF() {
        return new Function<String, String>() {
            public String apply(String a) {
                return a.toUpperCase();
            }
        };
    }
    
    public static Function<String, String> trimF() {
        return new Function<String, String>() {
            public String apply(String a) {
                return a.trim();
            }
        };
    }
    
    /**
     * Split string using the specified regex.
     * 
     * @see Pattern#split(CharSequence)
     */
    public static Function<String, ListF<String>> splitF(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return new Function<String, ListF<String>>() {
            public ListF<String> apply(String a) {
                return Cf.list(pattern.split(a));
            }
        };
    }

    /**
     * Split string into two strings using the specified regex.
     * 
     * @see Pattern#split(CharSequence, int)
     */
	public static Function<String, Tuple2<String, String>> split2F(String regex) {
    	final Pattern pattern = Pattern.compile(regex);
        return new Function<String, Tuple2<String, String>>() {
            public Tuple2<String, String> apply(String a) {
                String[] array = pattern.split(a, 2);
                return Tuple2.tuple(array[0], array[1]);
            }
    	};
    }
	
	/**
     * Split string into three strings using the specified regex.
     * 
     * @see Pattern#split(CharSequence, int)
     */
	public static Function<String, Tuple3<String, String, String>> split3F(String regex) {
    	final Pattern pattern = Pattern.compile(regex);
        return new Function<String, Tuple3<String,String,String>>() {
            public Tuple3<String, String, String> apply(String a) {
                String[] array = pattern.split(a, 3);
                return Tuple3.tuple(array[0], array[1], array[2]);
            }
    	};
    }
	
	/**
	 * Split string into four strings using the specified regex.
     * 
     * @see Pattern#split(CharSequence, int)
     */
	public static Function<String, Tuple4<String, String, String, String>> split4F(String regex) {
    	final Pattern pattern = Pattern.compile(regex);
        return new Function<String, Tuple4<String, String, String, String>>() {
            public Tuple4<String, String, String, String> apply(String a) {
                String[] array = pattern.split(a, 4);
                return Tuple4.tuple(array[0], array[1], array[2], array[3]);
            }
    	};
    }
    
} //~
