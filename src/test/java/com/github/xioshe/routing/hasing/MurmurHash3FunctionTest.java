package com.github.xioshe.routing.hasing;

import com.github.xioshe.routing.RoutingBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MurmurHash3FunctionTest extends RoutingBaseTest {

    @Test
    void Hash_gte_zero() {
        MurmurHash3Function function = new MurmurHash3Function();
        Assertions.assertTrue(function.hash("") >= 0);
        for (int i = 0; i < 10000; i++) {
            Assertions.assertTrue(function.hash(getRandString(8)) >= 0);
        }
    }

    @Test
    void UnitInterval_between_0_and_1() {
        MurmurHash3Function function = new MurmurHash3Function();
        for (int i = 0; i < 10000; i++) {
            var randString = getRandString(8);
            double hash = function.unitInterval(randString);
            assertThat(hash).as(randString).isGreaterThan(0).isLessThanOrEqualTo(1.0);
        }
    }

}