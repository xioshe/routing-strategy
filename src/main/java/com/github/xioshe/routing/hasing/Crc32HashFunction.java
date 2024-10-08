package com.github.xioshe.routing.hasing;

import java.nio.charset.StandardCharsets;

/**
 * CRC32c 算法属于非密码学哈希函数，性能更好。
 * <p/>
 * 一致性哈希算法对哈希函数的要求是快速、分布均匀、具备雪崩效应，对抗碰撞性要求不高。
 */
public class Crc32HashFunction implements HashFunction {

    private final com.google.common.hash.HashFunction crc32c = com.google.common.hash.Hashing.crc32c();

    @Override
    public long hash(String key) {
        return crc32c.hashString(key, StandardCharsets.UTF_8).padToLong();
    }

    @Override
    public int bits() {
        return 32;
    }
}
