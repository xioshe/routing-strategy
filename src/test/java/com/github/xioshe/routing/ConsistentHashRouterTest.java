package com.github.xioshe.routing;

import com.github.xioshe.routing.node.IpNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConsistentHashRouterTest extends RoutingBaseTest {

    @Test
    void Add_into_an_empty_ring_return_empty_list() {
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3);
        assertThat(router.addNode(new IpNode("127.0.0.1"))).isEmpty();
        assertThat(router.size()).isEqualTo(1);
        assertThat(router.virtualNodeSize()).isEqualTo(3);
    }

    @Test
    void Add_return_affected_node() {
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3);
        var node = new IpNode("127.0.0.1");
        router.setNodes(new IpNode[]{node});
        assertThat(router.size()).isEqualTo(1);
        assertThat(router.virtualNodeSize()).isEqualTo(3);

        var affected = router.addNode(new IpNode("127.0.0.2"));
        assertThat(router.size()).isEqualTo(2);
        assertThat(router.virtualNodeSize()).isEqualTo(6);
        assertThat(affected).containsOnlyOnce(node);
    }

    @Test
    void Add_return_affected_nodes() {
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3);
        var nodes = new IpNode[]{new IpNode("127.0.0.1"), new IpNode("127.0.0.2")};
        router.setNodes(nodes);
        assertThat(router.size()).isEqualTo(2);
        assertThat(router.virtualNodeSize()).isEqualTo(6);
        var affected = router.addNode(new IpNode("127.0.0.3"));
        assertThat(router.size()).isEqualTo(3);
        assertThat(router.virtualNodeSize()).isEqualTo(9);
        assertThat(affected).containsAnyOf(nodes);
    }

    @Test
    void Remove_from_an_empty_ring_return_empty_list() {
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3);
        assertThat(router.removeNode(new IpNode("127.0.0.1"))).isEmpty();
    }

    @Test
    void Remove_a_not_existing_node_return_empty_list() {
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3);
        var node = new IpNode("127.0.0.1");
        router.setNodes(new IpNode[]{node});
        assertThat(router.size()).isEqualTo(1);
        assertThat(router.virtualNodeSize()).isEqualTo(3);
        assertThat(router.removeNode(new IpNode("127.0.0.2"))).isEmpty();
    }

    @Test
    void Remove_return_removed_node() {
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3);
        var node = new IpNode("127.0.0.1");
        router.setNodes(new IpNode[]{node, new IpNode("127.0.0.2")});
        var removed = router.removeNode(node);
        assertThat(removed).containsOnlyOnce(node);
    }

    @Test
    void Route_return_a_node() {
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3);
        var nodes = new IpNode[]{new IpNode("127.0.0.1"), new IpNode("127.0.0.2")};
        router.setNodes(nodes);
        assertThat(router.route("key")).isIn(nodes);
    }

    @Test
    void Route_with_an_empty_ring_return_null() {
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3);
        assertThat(router.route("key")).isNull();
    }


    @Test
    void Route_with_sequence_is_balanced() {
        IpNode[] nodes = new IpNode[3];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new IpNode("127.3.7." + i);
        }
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(100);
        router.setNodes(nodes);

        int[] counts = new int[nodes.length];

        for (int i = 0; i < 100_000; i++) {
            IpNode node = router.route(getNextSequence());
            var key = node.key();
            counts[key.charAt(key.length() - 1) - '0']++;
        }

        for (int count : counts) {
            assertThat(count).isBetween(20000, 40000);
        }
    }

    @Test
    void Route_with_random_string_is_balanced() {
        IpNode[] nodes = new IpNode[3];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new IpNode("127.3.7." + i);
        }
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(100);
        router.setNodes(nodes);

        int[] counts = new int[nodes.length];

        for (int i = 0; i < 100_000; i++) {
            IpNode node = router.route(getRandString(8));
            var key = node.key();
            counts[key.charAt(key.length() - 1) - '0']++;
        }

        for (int count : counts) {
            assertThat(count).isBetween(20000, 40000);
        }
    }
}