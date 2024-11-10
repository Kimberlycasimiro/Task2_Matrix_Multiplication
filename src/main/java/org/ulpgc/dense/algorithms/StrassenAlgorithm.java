package org.ulpgc.dense.algorithms;

public class StrassenAlgorithm {

    public static double[][] strassenMultiply(double[][] a, double[][] b) {
        int n = a.length;
        if (n <= 2) {
            return BasicMultiplication.basicMultiply(a, b);
        }

        int newSize = n / 2;
        double[][] a11 = new double[newSize][newSize];
        double[][] a12 = new double[newSize][newSize];
        double[][] a21 = new double[newSize][newSize];
        double[][] a22 = new double[newSize][newSize];
        double[][] b11 = new double[newSize][newSize];
        double[][] b12 = new double[newSize][newSize];
        double[][] b21 = new double[newSize][newSize];
        double[][] b22 = new double[newSize][newSize];

        splitMatrix(a, a11, 0, 0);
        splitMatrix(a, a12, 0, newSize);
        splitMatrix(a, a21, newSize, 0);
        splitMatrix(a, a22, newSize, newSize);
        splitMatrix(b, b11, 0, 0);
        splitMatrix(b, b12, 0, newSize);
        splitMatrix(b, b21, newSize, 0);
        splitMatrix(b, b22, newSize, newSize);

        double[][] m1 = strassenMultiply(add(a11, a22), add(b11, b22));
        double[][] m2 = strassenMultiply(add(a21, a22), b11);
        double[][] m3 = strassenMultiply(a11, subtract(b12, b22));
        double[][] m4 = strassenMultiply(a22, subtract(b21, b11));
        double[][] m5 = strassenMultiply(add(a11, a12), b22);
        double[][] m6 = strassenMultiply(subtract(a21, a11), add(b11, b12));
        double[][] m7 = strassenMultiply(subtract(a12, a22), add(b21, b22));

        double[][] result = new double[n][n];
        combineMatrix(add(subtract(add(m1, m4), m5), m7), result, 0, 0);
        combineMatrix(add(m3, m5), result, 0, newSize);
        combineMatrix(add(m2, m4), result, newSize, 0);
        combineMatrix(add(subtract(add(m1, m3), m2), m6), result, newSize, newSize);

        return result;
    }

    private static double[][] add(double[][] a, double[][] b) {
        int n = a.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    private static double[][] subtract(double[][] a, double[][] b) {
        int n = a.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    private static void splitMatrix(double[][] parent, double[][] child, int row, int col) {
        for (int i = 0; i < child.length; i++) {
            System.arraycopy(parent[i + row], col, child[i], 0, child.length);
        }
    }

    private static void combineMatrix(double[][] child, double[][] parent, int row, int col) {
        for (int i = 0; i < child.length; i++) {
            System.arraycopy(child[i], 0, parent[i + row], col, child.length);
        }
    }
}

