package com.github.xioshe.routing.hasing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class Crc32HashFunctionTest {

    @Test
    void shouldReturnGreaterThanOrEqualToZero() {
        Crc32HashFunction crc32 = new Crc32HashFunction();
        Assertions.assertTrue(crc32.hash("test") >= 0);
    }

    @Test
    void Hash_with_different_seed_return_different_hashcode() {
        // given
        Crc32HashFunction crc32 = new Crc32HashFunction();
        String seed1 = "seed_1";
        String seed2 = "seed_2";
        String key = "key";
        // when
        int hash1 = crc32.hash(seed1, key);
        int hash2 = crc32.hash(seed2, key);
        // then
        Assertions.assertNotEquals(hash1, hash2);
    }
}