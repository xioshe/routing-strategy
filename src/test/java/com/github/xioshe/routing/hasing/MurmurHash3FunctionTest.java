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

    @Test
    void Hash_with_different_seed_return_different_hashcode() {
        // given
        MurmurHash3Function murmurHash3 = new MurmurHash3Function();
        String seed1 = "seed_1";
        String seed2 = "seed_2";
        String key = "key";
        // when
        int hash1 = murmurHash3.hash(seed1, key);
        int hash2 = murmurHash3.hash(seed2, key);
        // then
        Assertions.assertNotEquals(hash1, hash2);
    }

}