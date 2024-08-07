package com.github.xioshe.routing.hasing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MurmurHash3FunctionTest {

    @Test
    void shouldReturnGreaterThanOrEqualToZero() {
        // given
        MurmurHash3Function murmurHash3 = new MurmurHash3Function();
        String key = "key";
        // when
        int hash = murmurHash3.hash(key);
        // then
        Assertions.assertTrue(hash >= 0);
    }

}