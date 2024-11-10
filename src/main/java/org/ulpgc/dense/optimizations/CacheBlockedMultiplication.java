package org.ulpgc.dense.optimizations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheBlockedMultiplication {

    public static double[][] cacheBlockedMultiply(double[][] a, double[][] b, int blockSize) {
        int n = a.length;
        double[][] result = new double[n][n];
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < n; i += blockSize) {
            for (int j = 0; j < n; j += blockSize) {
                for (int k = 0; k < n; k += blockSize) {
                    final int rowStart = i;
                    final int colStart = j;
                    final int kStart = k;

                    executor.submit(() -> {
                        int rowEnd = Math.min(rowStart + blockSize, n);
                        int colEnd = Math.min(colStart + blockSize, n);
                        int kEnd = Math.min(kStart + blockSize, n);

                        for (int ii = rowStart; ii < rowEnd; ii++) {
                            for (int jj = colStart; jj < colEnd; jj++) {
                                double sum = 0;
                                for (int kk = kStart; kk < kEnd; kk++) {
                                    sum += a[ii][kk] * b[kk][jj];
                                }
                                synchronized (result) {
                                    result[ii][jj] += sum;
                                }
                            }
                        }
                    });
                }
            }
        }

        executor.shutdown();
        return result;
    }
}
