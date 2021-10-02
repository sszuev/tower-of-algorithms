package com.gitlab.sszuev.benchmarks.maps;

import com.gitlab.sszuev.maps.AVLBinarySearchTreeSimpleMap;
import com.gitlab.sszuev.maps.BinarySearchTreeSimpleMap;
import com.gitlab.sszuev.maps.JDKMapWrapperSimpleMap;
import com.gitlab.sszuev.maps.SimpleMap;

import java.util.TreeMap;

/**
 * Created by @ssz on 26.09.2021.
 */
@SuppressWarnings("unused")
public enum SimpleMapFactory {
    SIMPLE_BST {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new BinarySearchTreeSimpleMap<>();
        }
    },
    AVL_BST {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new AVLBinarySearchTreeSimpleMap<>();
        }
    },
    JDK_TREE_MAP {
        @SuppressWarnings("SortedCollectionWithNonComparableKeys")
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new JDKMapWrapperSimpleMap<>(new TreeMap<>());
        }
    },
    ;

    public abstract <K, V> SimpleMap<K, V> createEmptyMap();
}
