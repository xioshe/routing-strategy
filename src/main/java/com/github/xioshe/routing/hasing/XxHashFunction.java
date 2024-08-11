package com.github.xioshe.routing.hasing;

import org.apache.commons.codec.digest.XXHash32;

/**
 * xxHash32
 */
public class XxHashFunction implements HashFunction {

    @Override
    public long hash(String key) {
        XXHash32 xxHash32 = new XXHash32();
        xxHash32.update(key.getBytes());
        return xxHash32.getValue();
    }

    @Override
    public int bits() {
        return 32;
    }
}
