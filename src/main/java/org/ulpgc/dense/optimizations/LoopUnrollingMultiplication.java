package org.ulpgc.dense.optimizations;

public class LoopUnrollingMultiplication {

    public static double[][] loopUnrolledMultiply(double[][] a, double[][] b) {
        int n = a.length;
        double[][] result = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k += 4) {
                    sum += a[i][k] * b[k][j];
                    if (k + 1 < n) sum += a[i][k + 1] * b[k + 1][j];
                    if (k + 2 < n) sum += a[i][k + 2] * b[k + 2][j];
                    if (k + 3 < n) sum += a[i][k + 3] * b[k + 3][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }
}
