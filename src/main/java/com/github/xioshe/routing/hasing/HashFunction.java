package com.github.xioshe.routing.hasing;


/**
 * 哈希函数接口
 */
public interface HashFunction {

    /**
     * 只需要 32 位
     *
     * @param key 计算哈希值的字符串
     * @return 哈希值
     */
    int hash(String key);
}
