package com.github.xioshe.routing.hasing;

import org.apache.commons.codec.digest.XXHash32;

/**
 * xxHash32
 */
public class XxHashFunction implements HashFunction {

    @Override
    public int hash(String key) {
        XXHash32 xxHash32 = new XXHash32();
        xxHash32.update(key.getBytes());
        // 保证返回值为正数
        return (int) (xxHash32.getValue() & 0x7fffffff);
    }
}
