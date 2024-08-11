package com.github.xioshe.routing.hasing;


import java.nio.charset.StandardCharsets;

/**
 * 128 bit MurmurHash3 hash function.
 */
public class MurmurHash3Function implements HashFunction {

    private final com.google.common.hash.HashFunction murMurHash3 = com.google.common.hash.Hashing.murmur3_128();

    @Override
    public long hash(String key) {
        // mmr3 结果为 128 位，保证返回值为正数
        long hash = murMurHash3.hashString(key, StandardCharsets.UTF_8).asLong();
        return hash & 0x7FFFFFFFFFFFFFFFL;
    }

    @Override
    public int bits() {
        // mmr3 结果为 128 位，但 hash 函数返回的是 long，所以需要截取正数部分 63 位
        return 63;
    }

    @Override
    public double unitInterval(String key) {
        // 1 << 63L 是负数
        return (hash(key) + 1) / Math.pow(2, bits());
    }
}
