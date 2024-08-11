package com.github.xioshe.routing.hasing;

import com.github.xioshe.routing.RoutingBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class XxHashFunctionTest extends RoutingBaseTest {

    @Test
    void Hash_gte_zero() {
        XxHashFunction function = new XxHashFunction();
        Assertions.assertTrue(function.hash("") >= 0);
        for (int i = 0; i < 10000; i++) {
            Assertions.assertTrue(function.hash(getRandString(8)) >= 0);
        }
    }

    @Test
    void UnitInterval_between_0_and_1() {
        XxHashFunction function = new XxHashFunction();
        for (int i = 0; i < 10000; i++) {
            double hash = function.unitInterval(getRandString(8));
            assertThat(hash).isGreaterThan(0).isLessThanOrEqualTo(1.0);
        }
    }
}