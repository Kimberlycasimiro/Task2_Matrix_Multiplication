package org.ulpgc.dense.optimizations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelMultiplication {

    public static double[][] parallelMultiply(double[][] a, double[][] b, int numThreads) {
        int n = a.length;
        double[][] result = new double[n][n];
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < n; i++) {
            final int row = i;
            executor.submit(() -> {
                for (int j = 0; j < n; j++) {
                    double sum = 0;
                    for (int k = 0; k < n; k++) {
                        sum += a[row][k] * b[k][j];
                    }
                    result[row][j] = sum;
                }
            });
        }

        executor.shutdown();
        return result;
    }
}
