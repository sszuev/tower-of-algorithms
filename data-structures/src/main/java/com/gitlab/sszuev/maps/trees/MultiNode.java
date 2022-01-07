package com.gitlab.sszuev.maps.trees;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * A technical interface for a B-node.
 * Used in B-tree.
 * <p>
 * Created by @ssz on 13.11.2021.
 *
 * @param <X> - payload
 */
public interface MultiNode<X> extends TreeNode {
    Stream<X> keys();

    @Override
    Stream<MultiNode<X>> children();

    default long size() {
        AtomicLong res = new AtomicLong();
        postOrder(x -> res.addAndGet(((MultiNode<?>) x).keys().count()));
        return res.get();
    }
}
