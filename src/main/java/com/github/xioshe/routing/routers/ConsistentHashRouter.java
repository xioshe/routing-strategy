package com.github.xioshe.routing.routers;

import com.github.xioshe.routing.ClusterAwareRouter;
import com.github.xioshe.routing.hasing.HashFunction;
import com.github.xioshe.routing.hasing.MurmurHash3Function;
import com.github.xioshe.routing.node.Node;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 一致性 hash 路由实现
 */
public class ConsistentHashRouter<T extends Node> implements ClusterAwareRouter<T> {

    /**
     * 用 TreeMap 简化 hashRing 结构，加快查询相邻节点速度
     */
    private final TreeMap<Integer, VirtualNode<T>> hashRing = new TreeMap<>();
    private final Map<T, List<VirtualNode<T>>> virtualNodeCache = new HashMap<>();
    private final HashFunction hashfunction;

    /**
     * 每个物理节点对应的虚拟节点数量
     */
    private final int virtualNodeCount;

    public ConsistentHashRouter(int virtualNodeCount) {
        this(new MurmurHash3Function(), virtualNodeCount);
    }

    public ConsistentHashRouter(HashFunction hashfunction, int virtualNodeCount) {
        this.hashfunction = hashfunction;
        this.virtualNodeCount = virtualNodeCount;
    }

    @Override
    public void setNodes(T[] nodes) {
        hashRing.clear();
        virtualNodeCache.clear();
        for (T node : nodes) {
            addNode(node);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> addNode(T node) {
        boolean isFirstNode = hashRing.isEmpty();
        Set<T> affectedNodes = new HashSet<>();
        for (int i = 0; i < virtualNodeCount; i++) {
            VirtualNode<T> virtualNode = new VirtualNode<>(node, node.key() + "#" + i);
            int hash = hashfunction.hash((virtualNode.key()));
            hashRing.put(hash, virtualNode);

            virtualNodeCache.putIfAbsent(node, new ArrayList<>());
            virtualNodeCache.get(node).add(virtualNode);

            if (!isFirstNode) {
                // 收集被影响的真实节点
                affectedNodes.add((T) previousVirtualNode(hash).physicalNode());
            }
        }
        return affectedNodes.stream().toList();
    }

    @Override
    public List<T> removeNode(T node) {
        List<VirtualNode<T>> virtualNodes = virtualNodeCache.get(node);
        if (virtualNodes == null) {
            return Collections.emptyList();
        }
        for (VirtualNode<T> virtualNode : virtualNodes) {
            int hash = hashfunction.hash((virtualNode.key()));
            hashRing.remove(hash);
        }
        virtualNodeCache.remove(node);
        return Collections.singletonList(node);
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public T route(String key) {
        if (hashRing.isEmpty()) {
            return null;
        }
        int hash = hashfunction.hash(key);
        VirtualNode<T> nextVirtualNode = nextVirtualNode(hash);
        return (T) nextVirtualNode.physicalNode();
    }

    @Override
    public int size() {
        return virtualNodeCache.size();
    }

    public int virtualNodeSize() {
        return hashRing.size();
    }

    private VirtualNode<T> previousVirtualNode(int hash) {
        Map.Entry<Integer, VirtualNode<T>> entry = hashRing.lowerEntry(hash);
        if (entry == null) {
            entry = hashRing.lastEntry();
        }
        return entry.getValue();
    }

    private VirtualNode<T> nextVirtualNode(int hash) {
        Map.Entry<Integer, VirtualNode<T>> entry = hashRing.higherEntry(hash);
        if (entry == null) {
            entry = hashRing.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 虚拟节点
     *
     * @param <T> 物理节点类型
     */
    private static final class VirtualNode<T extends Node> implements Node {
        private final T physicalNode;
        private final String key;

        private VirtualNode(T physicalNode, String key) {
            this.physicalNode = physicalNode;
            this.key = key;
        }

        public Node physicalNode() {
            return physicalNode;
        }

        @Override
        public String key() {
            return key;
        }
    }
}
