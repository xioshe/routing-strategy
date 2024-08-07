package com.github.xioshe.routing;

import com.github.xioshe.routing.node.Node;

/**
 * 路由接口
 *
 * @param <T> 节点类型
 */
public interface Router<T extends Node> {

    /**
     * 根据 key 选择节点
     *
     * @param key 数据的 key
     * @return Node
     */
    T route(String key);
}
