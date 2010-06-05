package ru.yandex.bolts.weaving;

import ru.yandex.bolts.collection.Tuple2List;

/**
 * @author Stepan Koltsov
 */
public class ClassTransformationResult {
    private final byte[] transformedClass;

    private final Tuple2List<String, byte[]> extraClasses;

    public ClassTransformationResult(byte[] transformedClass, Tuple2List<String, byte[]> extraClasses) {
        this.transformedClass = transformedClass;
        this.extraClasses = extraClasses;
    }

    public byte[] getTransformedClass() {
        return transformedClass;
    }

    public Tuple2List<String, byte[]> getExtraClasses() {
        return extraClasses;
    }

} //~
