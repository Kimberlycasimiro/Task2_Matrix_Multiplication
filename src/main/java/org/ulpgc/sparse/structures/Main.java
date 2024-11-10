package org.ulpgc.sparse.structures;

import java.io.InputStream;
import org.ulpgc.utils.MatrixLoader;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sparse Matrix Multiplication Project");

        try (InputStream inputStream = Main.class.getResourceAsStream("/matrix_samples/mc2depi.mtx")) {
            if (inputStream == null) {
                throw new RuntimeException("Matrix file not found");
            }

            SparseMatrixCSR matrixA = MatrixLoader.loadMatrixFromMTX(inputStream);
            System.out.println("Matrix A loaded in CSR format.");

            SparseMatrixCSC matrixB = matrixA.toCSC();
            System.out.println("Matrix B converted to CSC format.");

            System.gc();
            long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            long startTime = System.nanoTime();

            SparseMatrixCSR result = MatrixMultiplicationCSRCSC.multiply(matrixA, matrixB);

            long endTime = System.nanoTime();
            long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            long memoryUsage = (endMemory - startMemory) / (1024 * 1024);
            System.out.println("Sparse matrix multiplication (CSR x CSC) completed.");
            System.out.println("Result matrix in CSR format has " + result.getNumRows() + " rows and " + result.getNumCols() + " columns.");
            System.out.println("Execution time: " + (endTime - startTime) / 1_000_000 + " ms");
            System.out.println("Memory usage during multiplication: " + memoryUsage + " MB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

