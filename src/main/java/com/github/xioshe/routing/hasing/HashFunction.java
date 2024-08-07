package com.github.xioshe.routing.hasing;


/**
 * 哈希函数接口
 */
@FunctionalInterface
public interface HashFunction {

    /**
     * 只需要 32 位
     *
     * @param key 计算哈希值的字符串
     * @return 哈希值，应该大于等于 0
     */
    int hash(String key);
}
