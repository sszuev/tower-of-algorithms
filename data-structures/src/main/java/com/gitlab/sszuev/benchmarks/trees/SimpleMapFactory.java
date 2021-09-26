package com.gitlab.sszuev.benchmarks.trees;

import com.gitlab.sszuev.trees.AVLTSimpleMap;
import com.gitlab.sszuev.trees.BSTSimpleMap;
import com.gitlab.sszuev.trees.JDKTreeSimpleMap;
import com.gitlab.sszuev.trees.SimpleMap;

/**
 * Created by @ssz on 26.09.2021.
 */
@SuppressWarnings("unused")
public enum SimpleMapFactory {
    SIMPLE_BST {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new BSTSimpleMap<>();
        }
    },
    AVL_BST {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new AVLTSimpleMap<>();
        }
    },
    JDK_TREE_MAP {
        @Override
        public <K, V> SimpleMap<K, V> createEmptyMap() {
            return new JDKTreeSimpleMap<>();
        }
    },
    ;

    public abstract <K, V> SimpleMap<K, V> createEmptyMap();
}
