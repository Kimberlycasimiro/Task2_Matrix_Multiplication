package org.ulpgc.dense.algorithms;

public class WinogradAlgorithm {

    public static double[][] multiply(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int rowsB = matrixB.length;
        int colsB = matrixB[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException("Incompatible matrix sizes for multiplication.");
        }

        double[][] result = new double[rowsA][colsB];

        double[] rowFactors = new double[rowsA];
        double[] colFactors = new double[colsB];

        for (int i = 0; i < rowsA; i++) {
            rowFactors[i] = 0;
            for (int j = 0; j < colsA / 2; j++) {
                rowFactors[i] += matrixA[i][2 * j] * matrixA[i][2 * j + 1];
            }
        }

        for (int i = 0; i < colsB; i++) {
            colFactors[i] = 0;
            for (int j = 0; j < rowsB / 2; j++) {
                colFactors[i] += matrixB[2 * j][i] * matrixB[2 * j + 1][i];
            }
        }

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                result[i][j] = -rowFactors[i] - colFactors[j];
                for (int k = 0; k < colsA / 2; k++) {
                    result[i][j] += (matrixA[i][2 * k] + matrixB[2 * k + 1][j]) *
                            (matrixA[i][2 * k + 1] + matrixB[2 * k][j]);
                }
            }
        }

        if (colsA % 2 == 1) {
            int lastCol = colsA - 1;
            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsB; j++) {
                    result[i][j] += matrixA[i][lastCol] * matrixB[lastCol][j];
                }
            }
        }

        return result;
    }
}
