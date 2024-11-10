package org.ulpgc.utils;

import org.ulpgc.sparse.optimization.SparseMatrix;
import java.util.Random;

public class MatrixGenerator {
    private static final Random random = new Random();

    public static double[][] generateDenseMatrix(int size) {
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextDouble() * 10;
            }
        }
        return matrix;
    }

    public static SparseMatrix generateSparseMatrix(int size, double sparsity) {
        SparseMatrix sparseMatrix = new SparseMatrix(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (random.nextDouble() > sparsity) {
                    sparseMatrix.addValue(i, j, random.nextDouble() * 10);
                }
            }
        }
        return sparseMatrix;
    }

    public static double[][] generateSparseMatrixAsDense(int size, double sparsity) {
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (random.nextDouble() > sparsity) {
                    matrix[i][j] = random.nextDouble() * 10;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
        return matrix;
    }
}
