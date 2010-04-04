package ru.yandex.bolts.weaving;

import ru.yandex.bolts.collection.ListMap;

/**
 * @author Stepan Koltsov
 */
public class ClassTransformationResult {
    private final byte[] transformedClass;

    private final ListMap<String, byte[]> extraClasses;

    public ClassTransformationResult(byte[] transformedClass, ListMap<String, byte[]> extraClasses) {
        this.transformedClass = transformedClass;
        this.extraClasses = extraClasses;
    }

    public byte[] getTransformedClass() {
        return transformedClass;
    }

    public ListMap<String, byte[]> getExtraClasses() {
        return extraClasses;
    }

} //~
