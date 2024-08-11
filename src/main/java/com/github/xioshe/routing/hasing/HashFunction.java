package com.github.xioshe.routing.hasing;


/**
 * 哈希函数接口
 */
public interface HashFunction {

    /**
     * 只需要 32 位
     *
     * @param key 计算哈希值的字符串
     * @return 哈希值，应该大于等于 0
     */
    long hash(String key);

    /**
     * 返回哈希值的位数
     *
     * @return 哈希值位数
     */
    int bits();

    /**
     * 返回 [0,1) 之间的 double 值
     *
     * @param key 计算哈希值的字符串
     * @return (0, 1] 之间的 double 值
     */
    default double unitInterval(String key) {
        return (hash(key) + 1.0) / (1L << bits());
    }
}
