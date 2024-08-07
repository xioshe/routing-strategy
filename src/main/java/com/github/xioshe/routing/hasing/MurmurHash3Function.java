package com.github.xioshe.routing.hasing;


/**
 * 128 bit MurmurHash3 hash function.
 */
public class MurmurHash3Function implements HashFunction {

    private final com.google.common.hash.HashFunction murMurHash3 = com.google.common.hash.Hashing.murmur3_128();

    @Override
    public int hash(String key) {
        // 保证返回值为正数
        return murMurHash3.hashString(key, java.nio.charset.StandardCharsets.UTF_8).asInt() & 0x7fffffff;
    }
}
