package com.github.xioshe.routing;

/**
 * 感知集群节点变化的路由接口
 *
 * @param <T> 节点类型
 */
public interface ClusterAwareRouter<T extends Node> extends Router<T>, ClusterAware<T> {

}
