package com.github.xioshe.routing;

import com.github.xioshe.routing.node.Node;

import java.util.List;

/**
 * 感知集群节点变化
 *
 * @param <T> 节点类型
 */
public interface ClusterAware<T extends Node> {
    
    /**
     * 添加节点
     *
     * @param node 返回需要调整的节点
     * @return 需要调整的节点
     */
    default List<T> addNode(T node) {
        return null;
    }

    /**
     * 删除节点，返回需要调整的节点
     *
     * @param node 需要删除的节点
     * @return 需要调整的节点
     */
    default List<T> removeNode(T node) {
        return null;
    }

    /**
     * 获取节点数量
     *
     * @return 节点数量
     */
    default int size() {
        return 0;
    }

}
