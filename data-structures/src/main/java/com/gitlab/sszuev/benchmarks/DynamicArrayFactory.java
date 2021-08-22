package com.gitlab.sszuev.benchmarks;

import com.gitlab.sszuev.arrays.*;

import java.util.ArrayList;
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
    },
    VECTOR_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new VectorDynamicArray<>();
        }
    },
    MATRIX_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new MatrixDynamicArray<>();
        }
    },
    FACTOR_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new FactorDynamicArray<>();
        }
    },

    JDK_ARRAY_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new JDKListDynamicArray<>(new ArrayList<>());
        }
    },
    JDK_LINKED_LIST {
        @Override
        DynamicArray<Object> createEmptyList() {
            return new JDKListDynamicArray<>(new LinkedList<>());
        }
    };

    abstract DynamicArray<Object> createEmptyList();
}
