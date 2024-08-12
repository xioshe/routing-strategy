package com.github.xioshe.routing.routers;

import com.github.xioshe.routing.RoutingBaseTest;
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
        var node = new IpNode("127.0.0.1");
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3, node);
        assertThat(router.size()).isEqualTo(1);
        assertThat(router.virtualNodeSize()).isEqualTo(3);

        var affected = router.addNode(new IpNode("127.0.0.2"));
        assertThat(router.size()).isEqualTo(2);
        assertThat(router.virtualNodeSize()).isEqualTo(6);
        assertThat(affected).containsOnlyOnce(node);
    }

    @Test
    void Add_return_affected_nodes() {
        var nodes = new IpNode[]{new IpNode("127.0.0.1"), new IpNode("127.0.0.2")};
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3, nodes);
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
        var node = new IpNode("127.0.0.1");
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3, node);
        assertThat(router.size()).isEqualTo(1);
        assertThat(router.virtualNodeSize()).isEqualTo(3);
        assertThat(router.removeNode(new IpNode("127.0.0.2"))).isEmpty();
    }

    @Test
    void Remove_return_removed_node() {
        var node = new IpNode("127.0.0.1");
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3, node);
        var removed = router.removeNode(node);
        assertThat(removed).containsOnlyOnce(node);
    }

    @Test
    void Route_return_a_node() {
        var nodes = new IpNode[]{new IpNode("127.0.0.1"), new IpNode("127.0.0.2")};
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(3, nodes);
        assertThat(router.route("key")).isIn((Object[]) nodes);
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
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(200, nodes);

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
        ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(200, nodes);

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

    @Test
    void Route_with_different_virtualNodeSize() {
        var values = new int[]{1, 3, 10, 50, 100, 200, 1000, 10000};
        for (int virtualNodeSize : values) {
            IpNode[] nodes = new IpNode[3];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new IpNode("" + i);
            }
            ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(virtualNodeSize, nodes);
            int[] counts = new int[nodes.length];
            for (int i = 0; i < 100_000; i++) {
                IpNode node = router.route(getNextSequence());
                counts[node.key().charAt(0) - '0']++;
            }
            System.out.println(virtualNodeSize + ", " + new ArraySummaryStatistics(counts).summary());
        }
    }

    @Test
    void Route_with_different_key_size() {
        var amounts = new int[]{10000, 10_0000, 100_0000, 500_0000};
        for (int amount : amounts) {
            IpNode[] nodes = new IpNode[3];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new IpNode("" + i);
            }
            ConsistentHashRouter<IpNode> router = new ConsistentHashRouter<>(1000, nodes);
            int[] counts = new int[nodes.length];
            for (int i = 0; i < amount; i++) {
                IpNode node = router.route(getNextSequence());
                counts[node.key().charAt(0) - '0']++;
            }
            System.out.println(amount + ", " + new ArraySummaryStatistics(counts).summary());
        }
    }
}