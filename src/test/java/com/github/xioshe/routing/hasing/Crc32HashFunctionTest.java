package com.github.xioshe.routing.hasing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class Crc32HashFunctionTest {

    @Test
    void shouldReturnGreaterThanOrEqualToZero() {
        Crc32HashFunction crc32 = new Crc32HashFunction();
        Assertions.assertTrue(crc32.hash("test") >= 0);
    }

}