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
        public DynamicArray<Object> createEmptyList() {
            return new SimpleDynamicArray<>();
        }

        @Override
        public DynamicArray<Object> createListOf(Object... data) {
            return SimpleDynamicArray.of(data);
        }
    },
    VECTOR_LIST {
        @Override
        public DynamicArray<Object> createEmptyList() {
            return new FixedVectorDynamicArray<>();
        }

        @Override
        public DynamicArray<Object> createListOf(Object... data) {
            return FixedVectorDynamicArray.of(10, data);
        }
    },
    MATRIX_LIST {
        @Override
        public DynamicArray<Object> createEmptyList() {
            return new MatrixDynamicArray<>();
        }

        @Override
        public DynamicArray<Object> createListOf(Object... data) {
            return MatrixDynamicArray.of(10, data);
        }
    },
    FACTOR_LIST {
        @Override
        public DynamicArray<Object> createEmptyList() {
            return new FactorVectorDynamicArray<>();
        }

        @Override
        public DynamicArray<Object> createListOf(Object... data) {
            return FactorVectorDynamicArray.of(50, data);
        }
    },

    JDK_ARRAY_LIST {
        @Override
        public DynamicArray<Object> createEmptyList() {
            return new JDKListDynamicArray<>(new ArrayList<>());
        }

        @Override
        public DynamicArray<Object> createListOf(Object... data) {
            return new JDKListDynamicArray<>(new ArrayList<>(Arrays.asList(data)));
        }
    },
    JDK_LINKED_LIST {
        @Override
        public DynamicArray<Object> createEmptyList() {
            return new JDKListDynamicArray<>(new LinkedList<>());
        }

        @Override
        public DynamicArray<Object> createListOf(Object... data) {
            return new JDKListDynamicArray<>(new LinkedList<>(Arrays.asList(data)));
        }
    };

    public abstract DynamicArray<Object> createEmptyList();

    public abstract DynamicArray<Object> createListOf(Object... data);
}
