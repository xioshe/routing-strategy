package com.github.xioshe.routing.hasing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class XxHashFunctionTest {

    @Test
    void shouldReturnGreaterThanOrEqualToZero() {
        assertThat(new XxHashFunction().hash("test")).isGreaterThanOrEqualTo(0);
    }

    @Test
    void Hash_with_different_seed_return_different_hashcode() {
        // given
        XxHashFunction xxh = new XxHashFunction();
        String seed1 = "seed_1";
        String seed2 = "seed_2";
        String key = "key";
        // when
        int hash1 = xxh.hash(seed1, key);
        int hash2 = xxh.hash(seed2, key);
        // then
        Assertions.assertNotEquals(hash1, hash2);
    }
}