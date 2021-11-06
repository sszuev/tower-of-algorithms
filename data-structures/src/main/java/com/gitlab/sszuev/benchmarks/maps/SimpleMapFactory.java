package com.gitlab.sszuev.benchmarks.maps;

import com.gitlab.sszuev.maps.*;

import java.util.HashMap;
import java.util.Hashtable;
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
    TREAP_BST {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new TreapSimpleMap<>();
        }
    },

    SEPARATE_CHAINING_HASHTABLE {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new SeparateChainingHashtableSimpleMap<>();
        }
    },
    OPEN_ADDRESSING_HASHTABLE {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new OpenAddressingHashtableSimpleMap<>();
        }
    },
    JDK_TREE_MAP {
        @SuppressWarnings("SortedCollectionWithNonComparableKeys")
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new JDKMapWrapperSimpleMap<>(new TreeMap<>());
        }
    },
    JDK_HASHMAP {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new JDKMapWrapperSimpleMap<>(new HashMap<>());
        }
    },
    JDK_HASHTABLE {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new JDKMapWrapperSimpleMap<>(new Hashtable<>());
        }
    },
    ;

    public abstract <K, V> SimpleMap<K, V> createEmptyMap();
}
