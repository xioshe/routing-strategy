package com.github.xioshe.routing.routers;

import com.github.xioshe.routing.ClusterAwareRouter;
import com.github.xioshe.routing.node.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询路由实现
 *
 * @param <T> 节点类型
 */
public class RoundRobinRouter<T extends Node> implements ClusterAwareRouter<T> {

    private final List<T> nodes = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @SafeVarargs
    public RoundRobinRouter(T... nodes) {
        Collections.addAll(this.nodes, nodes);
    }

    @Override
    public List<T> addNode(T node) {
        nodes.add(node);
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public List<T> removeNode(T node) {
        var snapshot = new ArrayList<>(nodes);
        nodes.remove(node);
        return snapshot;
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public T route(String key) {
        if (nodes.isEmpty()) {
            return null;
        }
        int index = (int) Math.abs(counter.getAndIncrement() % nodes.size());
        return nodes.get(index);
    }
}
