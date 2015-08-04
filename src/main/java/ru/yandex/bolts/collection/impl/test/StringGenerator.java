package ru.yandex.bolts.collection.impl.test;



public class StringGenerator extends Generator<String> {

    private final Generator<Integer> lengths;
    private final CharSequence alphabet;

    public StringGenerator(CharSequence alphabet, Generator<Integer> lengths) {
        this.lengths = lengths;
        this.alphabet = alphabet;
    }

    public char nextChar(CharSequence alphabet) {
        return alphabet.charAt(random.nextInt(alphabet.length()));
    }

    private String nextString(int length, CharSequence alphabet) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            sb.append(nextChar(alphabet));
        }
        return sb.toString();
    }

    @Override
    public String next() {
        return nextString(lengths.next(), alphabet);
    }

} //~
