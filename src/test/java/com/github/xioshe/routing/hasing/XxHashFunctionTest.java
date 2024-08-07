package com.github.xioshe.routing.hasing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class XxHashFunctionTest {

    @Test
    void shouldReturnGreaterThanOrEqualToZero() {
        assertThat(new XxHashFunction().hash("test")).isGreaterThanOrEqualTo(0);
    }
}