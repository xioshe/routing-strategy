package com.github.xioshe.routing.node;

public interface WeightedNode extends Node {

    default double getWeight() {
        return 1.0;
    }
}
