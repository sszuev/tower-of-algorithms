package com.gitlab.sszuev.maps.trees;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * A technical interface for a bi-node.
 * Used in BST.
 * <p>
 * Created by @ssz on 17.11.2021.
 */
public interface BiNode<X> extends TreeNode {

    X key();

    BiNode<X> left();

    BiNode<X> right();

    @Override
    default Stream<? extends BiNode<X>> children() {
        return Stream.of(left(), right());
    }

    default long size() {
        AtomicLong res = new AtomicLong();
        postOrder(x -> res.incrementAndGet());
        return res.get();
    }
}
