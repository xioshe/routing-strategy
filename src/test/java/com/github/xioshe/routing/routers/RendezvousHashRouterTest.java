package com.github.xioshe.routing.routers;

import com.github.xioshe.routing.RoutingBaseTest;
import com.github.xioshe.routing.node.IpNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

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

    @Test
    void Route_with_different_weight() {
        // given
        IpNode[] nodes = new IpNode[3];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new IpNode("127.0.0." + i, i + 1);
        }
        RendezvousHashRouter<IpNode> router = new RendezvousHashRouter<>(nodes);
        // when
        int[] counts = new int[nodes.length];
        for (int i = 0; i < 100_000; i++) {
            IpNode node = router.route(getNextSequence());
            var key = node.key();
            counts[key.charAt(key.length() - 1) - '0']++;
        }
        // then
        var sum = Arrays.stream(counts).sum();
        assertThat((double) counts[0] / sum).isCloseTo(1.0 / 6, withinPercentage(1));
        assertThat((double) counts[1] / sum).isCloseTo(2.0 / 6, withinPercentage(1));
        assertThat((double) counts[2] / sum).isCloseTo(3.0 / 6, withinPercentage(1));
    }
}