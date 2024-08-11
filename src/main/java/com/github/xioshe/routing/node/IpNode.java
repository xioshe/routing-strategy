package com.github.xioshe.routing.node;


/**
 * 以 IP 区分的节点类型
 */
public class IpNode implements WeightedNode {
    private final String ipAddress;
    private final double weight;

    public IpNode(String ipAddress) {
        this(ipAddress, 1.0);
    }

    public IpNode(String ipAddress, double weight) {
        this.ipAddress = ipAddress;
        this.weight = weight;
    }

    @Override
    public String key() {
        return ipAddress;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}
