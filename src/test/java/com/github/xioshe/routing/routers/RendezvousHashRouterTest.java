package com.github.xioshe.routing.routers;

import com.github.xioshe.routing.RoutingBaseTest;
import com.github.xioshe.routing.node.IpNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RendezvousHashRouterTest extends RoutingBaseTest {


    @Test
    void Route_with_sequence_is_balanced() {
        IpNode[] nodes = new IpNode[3];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new IpNode("127.0.0." + i);
        }
        RendezvousHashRouter<IpNode> router = new RendezvousHashRouter<>(nodes);

        int[] counts = new int[nodes.length];

        for (int i = 0; i < 100_000; i++) {
            IpNode node = router.route(getNextSequence());
            var key = node.key();
            counts[key.charAt(key.length() - 1) - '0']++;
        }

        for (int count : counts) {
            assertThat(count).isBetween(31000, 35000);
        }
    }

    @Test
    void Route_with_random_string_is_balanced() {
        IpNode[] nodes = new IpNode[3];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new IpNode("127.0.0." + i);
        }
        RendezvousHashRouter<IpNode> router = new RendezvousHashRouter<>(nodes);

        int[] counts = new int[nodes.length];

        for (int i = 0; i < 100_000; i++) {
            IpNode node = router.route(getRandString(8));
            var key = node.key();
            counts[key.charAt(key.length() - 1) - '0']++;
        }

        for (int count : counts) {
            assertThat(count).isBetween(31000, 35000);
        }
    }
}