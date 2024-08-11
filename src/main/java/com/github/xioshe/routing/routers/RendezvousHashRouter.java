package com.github.xioshe.routing.routers;

import com.github.xioshe.routing.ClusterAwareRouter;
import com.github.xioshe.routing.hasing.HashFunction;
import com.github.xioshe.routing.hasing.MurmurHash3Function;
import com.github.xioshe.routing.node.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * 集合点哈希算法实现
 *
 * @param <T> 节点类型
 */
public class RendezvousHashRouter<T extends Node> implements ClusterAwareRouter<T> {

    /**
     * 记录 Node 的有序集合，使用 ConcurrentSkipListSet 保证线程安全
     */
    private final ConcurrentSkipListSet<T> nodeList = new ConcurrentSkipListSet<>(new NodeComparator());
    private final HashFunction hashfunction;

    @SafeVarargs
    public RendezvousHashRouter(T... nodes) {
        this(new MurmurHash3Function(), nodes);
    }

    @SafeVarargs
    public RendezvousHashRouter(HashFunction hashfunction, T... nodes) {
        this.hashfunction = hashfunction;
        nodeList.addAll(Arrays.asList(nodes));
    }

    @Override
    public List<T> addNode(T node) {
        nodeList.add(node);
        // 添加新节点可能改变已经存储的 key 的节点序列，如果要确定影响范围，必须遍历所有节点
        return new ArrayList<>(nodeList);
    }

    @Override
    public List<T> removeNode(T node) {
        return nodeList.remove(node) ? Collections.singletonList(node) : Collections.emptyList();
    }

    /**
     * 跳表获取 size 的时间复杂度为 O(n)
     *
     * @return Size of nodes
     */
    @Override
    public int size() {
        return nodeList.size();
    }

    @Override
    public T route(String key) {
        if (nodeList.isEmpty()) {
            return null;
        }

        T result = null;
        int hash = Integer.MIN_VALUE;
        for (T node : nodeList) {
            int nodeHash = hashfunction.hash(key, node.key());
            if (nodeHash > hash) {
                hash = nodeHash;
                result = node;
            }
        }
        return result;
    }

    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.key().compareTo(o2.key());
        }
    }
}
