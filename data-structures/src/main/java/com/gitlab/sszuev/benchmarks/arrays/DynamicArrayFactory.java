package com.gitlab.sszuev.benchmarks.arrays;

import com.gitlab.sszuev.arrays.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * A factory to produce different kinds of{@link DynamicArray}s.
 * Created by @ssz on 22.08.2021.
 */
@SuppressWarnings("unused")
public enum DynamicArrayFactory {
    SIMPLE_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new SimpleDynamicArray<>();
        }

        @Override
        DynamicArray<Object> createListOf(Object... data) {
            return SimpleDynamicArray.of(data);
        }
    },
    VECTOR_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new FixedVectorDynamicArray<>();
        }

        @Override
        DynamicArray<Object> createListOf(Object... data) {
            return FixedVectorDynamicArray.of(10, data);
        }
    },
    MATRIX_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new MatrixDynamicArray<>();
        }

        @Override
        DynamicArray<Object> createListOf(Object... data) {
            return MatrixDynamicArray.of(10, data);
        }
    },
    FACTOR_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new FactorVectorDynamicArray<>();
        }

        @Override
        DynamicArray<Object> createListOf(Object... data) {
            return FactorVectorDynamicArray.of(50, data);
        }
    },

    JDK_ARRAY_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new JDKListDynamicArray<>(new ArrayList<>());
        }

        @Override
        DynamicArray<Object> createListOf(Object... data) {
            return new JDKListDynamicArray<>(new ArrayList<>(Arrays.asList(data)));
        }
    },
    JDK_LINKED_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new JDKListDynamicArray<>(new LinkedList<>());
        }

        @Override
        DynamicArray<Object> createListOf(Object... data) {
            return new JDKListDynamicArray<>(new LinkedList<>(Arrays.asList(data)));
        }
    };

    abstract DynamicArray<Object> createEmptyList();

    abstract DynamicArray<Object> createListOf(Object... data);
}
