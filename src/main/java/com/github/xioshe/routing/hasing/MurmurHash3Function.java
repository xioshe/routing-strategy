package com.github.xioshe.routing.hasing;


import java.nio.charset.StandardCharsets;

/**
 * 128 bit MurmurHash3 hash function.
 */
public class MurmurHash3Function implements HashFunction {

    private final com.google.common.hash.HashFunction murMurHash3 = com.google.common.hash.Hashing.murmur3_128();

    @Override
    public int hash(String key) {
        // 保证返回值为正数
        return murMurHash3.hashString(key, StandardCharsets.UTF_8).asInt() & 0x7fffffff;
    }

    @Override
    public int hash(String seed, String key) {
        return murMurHash3.newHasher()
                .putString(seed, StandardCharsets.UTF_8)
                .putString(key, StandardCharsets.UTF_8)
                .hash().asInt();
    }
}
