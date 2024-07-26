package com.github.xioshe.routing;

/**
 * 感知集群节点变化
 *
 * @param <T> 节点类型
 */
public interface ClusterAware<T extends Node> {

    void setNodes(T[] nodes);

    default void addNode(T node) {

    }

    default void removeNode(T node) {

    }

}
