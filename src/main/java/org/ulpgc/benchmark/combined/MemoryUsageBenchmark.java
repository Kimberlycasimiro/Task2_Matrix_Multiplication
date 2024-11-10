package org.ulpgc.benchmark.combined;

import org.ulpgc.dense.algorithms.BasicMultiplication;
import org.ulpgc.dense.algorithms.StrassenAlgorithm;
import org.ulpgc.dense.algorithms.WinogradAlgorithm;
import org.ulpgc.dense.optimizations.CacheBlockedMultiplication;
import org.ulpgc.dense.optimizations.LoopUnrollingMultiplication;
import org.ulpgc.dense.optimizations.ParallelMultiplication;
import org.ulpgc.dense.optimizations.VectorizedMultiplication;
import org.ulpgc.sparse.optimization.SparseMatrix;
import org.ulpgc.sparse.optimization.SparseMultiplication;
import org.ulpgc.utils.ConfigLoader;
import org.ulpgc.utils.MatrixGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MemoryUsageBenchmark {

    public static void main(String[] args) {
        ConfigLoader config = new ConfigLoader("config.properties");
        int[] matrixSizes = config.getIntArray("matrix.sizes");
        double[] sparsityLevels = config.getDoubleArray("sparsity.levels");
        int numThreads = config.getInt("num.threads");

        String outputPath = "src/main/java/org/ulpgc/benchmark/benchmarkResults/combined/combined_memory_usage_results.csv";

        try {
            Files.createDirectories(Paths.get("src/main/java/org/ulpgc/benchmark/benchmarkResults/combined"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.append("Method,MatrixType,MemoryUsageMb,MatrixSize,SparsityLevel\n");

            for (int matrixSize : matrixSizes) {
                for (double sparsityLevel : sparsityLevels) {
                    double[][] denseMatrixA = MatrixGenerator.generateSparseMatrixAsDense(matrixSize, sparsityLevel);
                    double[][] denseMatrixB = MatrixGenerator.generateSparseMatrixAsDense(matrixSize, sparsityLevel);

                    SparseMatrix sparseMatrixA = MatrixGenerator.generateSparseMatrix(matrixSize, sparsityLevel);
                    SparseMatrix sparseMatrixB = MatrixGenerator.generateSparseMatrix(matrixSize, sparsityLevel);

                    runAndSaveMemoryUsage("Basic Multiplication", "Dense",
                            () -> BasicMultiplication.basicMultiply(denseMatrixA, denseMatrixB), matrixSize, sparsityLevel, writer);
                    runAndSaveMemoryUsage("Cache Blocked Multiplication", "Dense",
                            () -> CacheBlockedMultiplication.cacheBlockedMultiply(denseMatrixA, denseMatrixB, calculateBlockSize(matrixSize)), matrixSize, sparsityLevel, writer);
                    runAndSaveMemoryUsage("Loop Unrolling Multiplication", "Dense",
                            () -> LoopUnrollingMultiplication.loopUnrolledMultiply(denseMatrixA, denseMatrixB), matrixSize, sparsityLevel, writer);
                    runAndSaveMemoryUsage("Parallel Multiplication", "Dense",
                            () -> ParallelMultiplication.parallelMultiply(denseMatrixA, denseMatrixB, numThreads), matrixSize, sparsityLevel, writer);
                    runAndSaveMemoryUsage("Strassen Multiplication", "Dense",
                            () -> StrassenAlgorithm.strassenMultiply(denseMatrixA, denseMatrixB), matrixSize, sparsityLevel, writer);
                    runAndSaveMemoryUsage("Vectorized Multiplication", "Dense",
                            () -> VectorizedMultiplication.vectorizedMultiply(denseMatrixA, denseMatrixB), matrixSize, sparsityLevel, writer);
                    runAndSaveMemoryUsage("Winograd Multiplication", "Dense",
                            () -> WinogradAlgorithm.multiply(denseMatrixA, denseMatrixB), matrixSize, sparsityLevel, writer);
                    runAndSaveMemoryUsage("Sparse Multiplication", "Sparse",
                            () -> SparseMultiplication.sparseMultiply(sparseMatrixA, sparseMatrixB), matrixSize, sparsityLevel, writer);
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

    private static int calculateBlockSize(int matrixSize) {
        if (matrixSize <= 128) return 8;
        else if (matrixSize <= 512) return 32;
        else if (matrixSize <= 1024) return 64;
        else return 128;
    }
}
