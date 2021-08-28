package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

/**
 * Created by @ssz on 22.08.2021.
 */
public class SimpleDynamicArrayTest extends DynamicArrayBaseTest {

    @Test
    public void testCreateNonEmpty() {
        doTestCreateNonEmpty(SimpleDynamicArray::of);
    }

    @Test
    public void testAdd() {
        doTestAdd(SimpleDynamicArray::of, 1, 2, 4);
        doTestAdd(SimpleDynamicArray::new);
    }

    @Test
    public void testInsert() {
        doTestInsert(SimpleDynamicArray::of, "a", "b", "c");
        doTestAdd(SimpleDynamicArray::new);
    }

    @Test
    public void testRandomModify() {
        doRandomModify(SimpleDynamicArray::new, 42);
        doRandomModify(SimpleDynamicArray::of, 120, LongStream.range(0, 3).boxed().toArray(Long[]::new));
    }

    @Test
    public void testRemove() {
        doTestRemove(SimpleDynamicArray::of);
    }
}
