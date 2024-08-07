package com.github.xioshe.routing.node;


/**
 * 以 IP 区分的节点类型
 */
public class IpNode implements Node {
    private final String ipAddress;

    public IpNode(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String key() {
        return ipAddress;
    }
}
