package com.github.xioshe.routing.routers;

import java.util.Arrays;

public class ArraySummaryStatistics {

    private final int[] array;
    private int sum;

    public ArraySummaryStatistics(int[] array) {
        this.array = array;
    }

    public int getSum() {
        if (sum == 0) {
            for (int i : array) {
                sum += i;
            }
        }

        return sum;
    }

    public double getAverage() {
        return (double) getSum() / array.length;
    }

    public double[] getProportions() {
        double[] proportions = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            proportions[i] = (double) array[i] / getSum();
        }
        return proportions;
    }


    public double stdDev() {
        double mean = getAverage();
        double sum = 0;
        for (int i : array) {
            sum += Math.pow(i - mean, 2);
        }
        return Math.sqrt(sum / array.length);
    }

    public double cv() {
        return stdDev() / getAverage();
    }

    public String summary() {
        return Arrays.toString(array) + ", " + Arrays.toString(getProportions()) + ", " + cv();
    }
}
