package org.ulpgc.dense.optimizations;

public class VectorizedMultiplication {

    public static double[][] vectorizedMultiply(double[][] a, double[][] b) {
        int n = a.length;
        double[][] result = new double[n][n];
        int blockSize = 64;

        for (int ii = 0; ii < n; ii += blockSize) {
            for (int jj = 0; jj < n; jj += blockSize) {
                for (int kk = 0; kk < n; kk += blockSize) {
                    for (int i = ii; i < Math.min(ii + blockSize, n); i++) {
                        for (int j = jj; j < Math.min(jj + blockSize, n); j++) {
                            double sum = 0;
                            for (int k = kk; k < Math.min(kk + blockSize, n); k++) {
                                sum += a[i][k] * b[k][j];
                            }
                            result[i][j] += sum;
                        }
                    }
                }
            }
        }

        return result;
    }
}
