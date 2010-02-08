package ru.yandex.bolts.collection.impl.test;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;

/**
 * @author Stepan Koltsov
 */
public class ListGenerator<A> extends Generator<ListF<A>> {

    private final Generator<A> elements;
    private final Generator<Integer> lengths;

    public ListGenerator(Generator<A> elementGenerator, Generator<Integer> lengthsGenerator) {
        this.elements = elementGenerator;
        this.lengths = lengthsGenerator;
    }

    @Override
    public ListF<A> next() {
        int length = lengths.next();
        ListF<A> r = Cf.arrayList(length);
        for (int i = 0; i < length; ++i) {
            r.add(elements.next());
        }
        return r;
    }

    public ListGenerator<A> maxLength(int length) {
        return new ListGenerator<A>(elements, Generator.ints(0, length + 1));
    }

} //~
