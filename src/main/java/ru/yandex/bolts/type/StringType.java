package ru.yandex.bolts.type;

import java.util.regex.Pattern;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;


/**
 * @author Stepan Koltsov
 */
public class StringType extends AnyCharSequenceType<String> {

    @Override
    public String plus(String a, String b) {
        return a + b;
    }

    @Override
    public String zero() {
        return "";
    }

    public Function2<String, String, String> joinF(String sep) {
        return plus3F().bind2(sep);
    }

    public Function<String, String> addSuffixF(String suffix) {
        return plusF().bind2(suffix);
    }

    public Function<String, String> addPrefixF(String prefix) {
        return plusF().bind1(prefix);
    }

    public Function2B<String, String> startsWithF() {
        return new Function2B<String, String>() {
            public boolean apply(String a, String b) {
                return a.startsWith(b);
            }
        };
    }

    public Function1B<String> startsWithF(String prefix) {
        return startsWithF().bind2(prefix);
    }

    public Function2B<String, String> endsWithF() {
        return new Function2B<String, String>() {
            public boolean apply(String a, String b) {
                return a.endsWith(b);
            }
        };
    }

    public Function1B<String> endsWithF(String suffix) {
        return endsWithF().bind2(suffix);
    }

    public Function<String, String> toLowerCaseF() {
        return new Function<String, String>() {
            public String apply(String a) {
                return a.toLowerCase();
            }
        };
    }

    public Function<String, String> toUpperCaseF() {
        return new Function<String, String>() {
            public String apply(String a) {
                return a.toUpperCase();
            }
        };
    }

    public Function<String, String> trimF() {
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
    public Function<String, ListF<String>> splitF(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return new Function<String, ListF<String>>() {
            public ListF<String> apply(String a) {
                return Cf.list(pattern.split(a));
            }
        };
    }

    public boolean equalsIgnoreCase(String a, String b) {
        if (a == null || b == null) {
            return a == b;
        } else {
            return a.equalsIgnoreCase(b);
        }
    }

    public Function2B<String, String> equalsIgnoreCaseF() {
        return new Function2B<String, String>() {
            public boolean apply(String a, String b) {
                return equalsIgnoreCase(a, b);
            }
        };
    }

    public Function1B<String> equalsIgnoreCaseF(String b) {
        return equalsIgnoreCaseF().bind2(b);
    }

} //~
