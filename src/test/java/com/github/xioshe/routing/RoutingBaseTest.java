package com.github.xioshe.routing;

import java.util.concurrent.ThreadLocalRandom;

public class RoutingBaseTest {

    private static final char[] letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345/".toCharArray();
    private final int seed = ThreadLocalRandom.current().nextInt(2 << 16);

    private int sequence = seed;

    /**
     * 生成顺序性 key，模拟时间戳、主键等
     */
    protected String getNextSequence() {
        return String.valueOf(sequence++);
    }


    /**
     * 生成随机字符串
     */
    protected String getRandString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(letters[ThreadLocalRandom.current().nextInt(letters.length)]);
        }
        return sb.toString();
    }
}