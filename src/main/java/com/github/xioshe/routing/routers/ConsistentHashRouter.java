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
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    /**
     * 读写锁，保证 hashRing 的线程安全
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    @SafeVarargs
    public ConsistentHashRouter(int virtualNodeCount, T... nodes) {
        this(new MurmurHash3Function(), virtualNodeCount, nodes);
    }

    @SafeVarargs
    public ConsistentHashRouter(HashFunction hashfunction, int virtualNodeCount, T... nodes) {
        this.hashfunction = hashfunction;
        this.virtualNodeCount = virtualNodeCount;
        for (T node : nodes) {
            doAddNode(node);
        }
    }

    @Override
    public List<T> addNode(T node) {
        lock.writeLock().lock();
        try {
            return doAddNode(node);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    private List<T> doAddNode(T node) {
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
        lock.writeLock().lock();
        try {
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
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public T route(String key) {
        lock.readLock().lock();
        try {
            if (hashRing.isEmpty()) {
                return null;
            }
            int hash = hashfunction.hash(key);
            VirtualNode<T> nextVirtualNode = nextVirtualNode(hash);
            return (T) nextVirtualNode.physicalNode();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return virtualNodeCache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public int virtualNodeSize() {
        lock.readLock().lock();
        try {
            return hashRing.size();
        } finally {
            lock.readLock().unlock();
        }
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
