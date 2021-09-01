package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Created by @ssz on 22.08.2021.
 */
public class MatrixDynamicArrayTest extends DynamicArrayTestBase {

    @Test
    public void testAdd() {
        doTestAdd(data -> MatrixDynamicArray.of(42, data), 42);
        doTestAdd(data -> new MatrixDynamicArray<>(33));
    }

    @Test
    public void testInsert() {
        doTestInsert(data -> MatrixDynamicArray.of(2, data),
                IntStream.range(0, 5).mapToObj(x -> "i=" + x).toArray(String[]::new));
        doTestInsert(data -> new MatrixDynamicArray<>());
    }

    @Test
    public void testRandomModify() {
        doRandomModify(data -> MatrixDynamicArray.of(21, data), 100, LongStream.range(0, 100).boxed().toArray(Long[]::new));
        doRandomModify(data -> MatrixDynamicArray.of(42, data), 42);
    }

    @Test
    public void testCreateNonEmptyWithVector4() {
        doTestCreateNonEmpty(data -> MatrixDynamicArray.of(42, data));
    }

    @Test
    public void testRemoveWithVector1() {
        doTestRemove(data -> MatrixDynamicArray.of(1, data));
    }

    @Test
    public void testRemoveWithVector2() {
        doTestRemove(data -> MatrixDynamicArray.of(2, data));
    }

    @Test
    public void testRemoveWithVector5() {
        doTestRemove(data -> MatrixDynamicArray.of(2, data));
    }

    @Test
    public void testRemoveWithVector10() {
        doTestRemove(data -> MatrixDynamicArray.of(10, data));
    }

}
