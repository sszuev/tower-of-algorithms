package com.gitlab.sszuev.maps;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by @ssz on 21.09.2021.
 */
public interface TreeNode<X> {

    Stream<TreeNode<X>> children();

    X key();

    default void preOrder(Consumer<TreeNode<X>> v) {
        v.accept(this);
        children().filter(Objects::nonNull).forEach(x -> x.preOrder(v));
    }

    default void postOrder(Consumer<TreeNode<X>> v) {
        children().filter(Objects::nonNull).forEach(x -> x.postOrder(v));
        v.accept(this);
    }

    default long size() {
        AtomicLong res = new AtomicLong();
        postOrder(xTreeNode -> res.incrementAndGet());
        return res.get();
    }
}
