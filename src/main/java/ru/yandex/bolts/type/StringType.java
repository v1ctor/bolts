package ru.yandex.bolts.type;

import java.util.regex.Pattern;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Tuple3;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;


public class StringType extends AnyCharSequenceType<String> {

    @Override
    public String plus(String a, String b) {
        return a + b;
    }

    @Override
    public String zero() {
        return "";
    }

    public Function2<String, Integer, Character> charAtF() {
        return String::charAt;
    }

    public Function2<String, Integer, Integer> codePointAtF() {
        return String::codePointAt;
    }

    public Function2B<String, CharSequence> containsF() {
        return String::contains;
    }

    public Function1B<String> containsF(CharSequence substring) {
        return containsF().bind2(substring);
    }

    public Function<String, String> internF() {
        return String::intern;
    }

    public Function<String, Integer> lengthF() {
        return String::length;
    }

    public Function1B<String> isEmptyF() {
        return s -> s == null || s.isEmpty();
    }

    public Function1B<String> isNotEmptyF() {
        return s -> s != null && !s.isEmpty();
    }

    public boolean isBlank(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }

        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public Function1B<String> isBlankF() {
        return this::isBlank;
    }

    public Function1B<String> isNotBlankF() {
        return this::isNotBlank;
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
        return String::startsWith;
    }

    public Function1B<String> startsWithF(String prefix) {
        return startsWithF().bind2(prefix);
    }

    public Function2B<String, String> endsWithF() {
        return String::endsWith;
    }

    public Function1B<String> endsWithF(String suffix) {
        return endsWithF().bind2(suffix);
    }

    public Function<String, String> toLowerCaseF() {
        return String::toLowerCase;
    }

    public Function<String, String> toUpperCaseF() {
        return String::toUpperCase;
    }

    public Function<String, String> trimF() {
        return String::trim;
    }


    public Function<String, ListF<String>> splitF(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return a -> Cf.list(pattern.split(a));
    }


    public Function<String, Tuple2<String, String>> split2F(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return a -> {
            String[] array = pattern.split(a, 2);
            return Tuple2.tuple(array[0], array[1]);
        };
    }


    public Function<String, Tuple3<String, String, String>> split3F(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return a -> {
            String[] array = pattern.split(a, 3);
            return Tuple3.tuple(array[0], array[1], array[2]);
        };
    }

    public Function1B<String> matchesF(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return string -> pattern.matcher(string).matches();
    }

    public boolean equalsIgnoreCase(String a, String b) {
        if (a == null || b == null) {
            return a == b;
        } else {
            return a.equalsIgnoreCase(b);
        }
    }

    public Function2B<String, String> equalsIgnoreCaseF() {
        return this::equalsIgnoreCase;
    }

    public Function1B<String> equalsIgnoreCaseF(String b) {
        return equalsIgnoreCaseF().bind2(b);
    }

} //~
