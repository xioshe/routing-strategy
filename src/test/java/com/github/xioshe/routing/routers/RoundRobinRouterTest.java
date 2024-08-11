package com.github.xioshe.routing.routers;

import com.github.xioshe.routing.node.IpNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoundRobinRouterTest {

    @Test
    void Add_return_all_nodes() {
        var nodes = new IpNode[]{
                new IpNode("127.0.0.1"),
                new IpNode("127.0.0.2"),
                new IpNode("127.0.0.3"),
                new IpNode("127.0.0.4")
        };
        RoundRobinRouter<IpNode> router = new RoundRobinRouter<>(nodes);

        var node = new IpNode("127.0.0.5");
        var ipNodes = new IpNode[nodes.length + 1];
        System.arraycopy(nodes, 0, ipNodes, 0, nodes.length);
        ipNodes[nodes.length] = node;
        assertThat(router.addNode(node)).containsExactly(ipNodes);
    }

    @Test
    void Remove_return_all_nodes() {
        var nodes = new IpNode[]{
                new IpNode("127.0.0.1"),
                new IpNode("127.0.0.2"),
                new IpNode("127.0.0.3"),
                new IpNode("127.0.0.4")
        };
        RoundRobinRouter<IpNode> router = new RoundRobinRouter<>(nodes);
        assertThat(router.removeNode(nodes[0])).containsExactly(nodes);
    }

    @Test
    void Route_is_not_null() {
        var nodes = new IpNode[]{
                new IpNode("127.0.0.1"),
                new IpNode("127.0.0.2"),
                new IpNode("127.0.0.3"),
                new IpNode("127.0.0.4")
        };
        RoundRobinRouter<IpNode> router = new RoundRobinRouter<>(nodes);
        assertThat(router.route("key")).isNotNull();
    }
}