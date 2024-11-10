package org.ulpgc.benchmark.sparse;

import org.ulpgc.sparse.optimization.SparseMatrix;
import org.ulpgc.sparse.optimization.SparseMultiplication;
import org.ulpgc.utils.ConfigLoader;
import org.ulpgc.utils.MatrixGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SparseMatrixMemoryUsage {

    public static void main(String[] args) {
        ConfigLoader config = new ConfigLoader("config.properties");
        int[] matrixSizes = config.getIntArray("matrix.sizes");
        double[] sparsityLevels = config.getDoubleArray("sparsity.levels");

        String outputPath = "src/main/java/org/ulpgc/benchmark/benchmarkResults/sparse/sparse_memory_usage_results.csv";

        try {
            Files.createDirectories(Paths.get("src/main/java/org/ulpgc/benchmark/benchmarkResults/sparse"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.append("Method,MatrixType,MemoryUsageMb,MatrixSize,SparsityLevel\n");

            for (int matrixSize : matrixSizes) {
                for (double sparsityLevel : sparsityLevels) {
                    SparseMatrix sparseMatrixA = MatrixGenerator.generateSparseMatrix(matrixSize, sparsityLevel);
                    SparseMatrix sparseMatrixB = MatrixGenerator.generateSparseMatrix(matrixSize, sparsityLevel);

                    runAndSaveMemoryUsage("Sparse Multiplication", "Sparse",
                            () -> SparseMultiplication.sparseMultiply(sparseMatrixA, sparseMatrixB),
                            matrixSize, sparsityLevel, writer);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runAndSaveMemoryUsage(String methodName, String matrixType, Runnable multiplicationMethod, int matrixSize, double sparsityLevel, FileWriter writer) {
        System.gc();

        long startMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);

        multiplicationMethod.run();

        long endMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        long memoryUsageMb = endMemory - startMemory;

        if (memoryUsageMb < 0) {
            memoryUsageMb = 0;
        }

        try {
            writer.append(methodName).append(",")
                    .append(matrixType).append(",")
                    .append(String.valueOf(memoryUsageMb)).append(",")
                    .append(String.valueOf(matrixSize)).append(",")
                    .append(String.valueOf(sparsityLevel)).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
