package com.gitlab.sszuev.maps.trees;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A technical interface for a node.
 * Used in BST.
 * <p>
 * Created by @ssz on 21.09.2021.
 */
public interface TreeNode {

    Stream<? extends TreeNode> children();

    default void preOrder(Consumer<TreeNode> v) {
        v.accept(this);
        children().filter(Objects::nonNull).forEach(x -> x.preOrder(v));
    }

    default void postOrder(Consumer<TreeNode> v) {
        children().filter(Objects::nonNull).forEach(x -> x.postOrder(v));
        v.accept(this);
    }
}
