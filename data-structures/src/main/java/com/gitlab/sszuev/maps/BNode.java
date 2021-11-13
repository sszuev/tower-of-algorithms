package com.gitlab.sszuev.maps;

import java.util.stream.Stream;

/**
 * A technical interface for a B-node.
 * Used in B-tree.
 * <p>
 * Created by @ssz on 13.11.2021.
 */
interface BNode<X> extends TreeNode<BNode<X>> {
    @Override
    Stream<BNode<X>> children();

    @Override
    default BNode<X> key() {
        return this;
    }
}
