package org.ulpgc.benchmark.combined;

import org.openjdk.jmh.annotations.*;
import org.ulpgc.dense.algorithms.BasicMultiplication;
import org.ulpgc.dense.algorithms.StrassenAlgorithm;
import org.ulpgc.dense.algorithms.WinogradAlgorithm;
import org.ulpgc.dense.optimizations.CacheBlockedMultiplication;
import org.ulpgc.dense.optimizations.LoopUnrollingMultiplication;
import org.ulpgc.dense.optimizations.ParallelMultiplication;
import org.ulpgc.dense.optimizations.VectorizedMultiplication;
import org.ulpgc.sparse.optimization.SparseMatrix;
import org.ulpgc.sparse.optimization.SparseMultiplication;
import org.ulpgc.utils.MatrixGenerator;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class MatrixMultiplicationBenchmark {

    @Param({"64", "128", "256", "512", "1024", "2048"})
    public int matrixSize;

    @Param({"0.0","0.3", "0.5", "0.8"})
    public double sparsityLevel;

    private double[][] denseMatrixA;
    private double[][] denseMatrixB;
    private SparseMatrix sparseMatrixA;
    private SparseMatrix sparseMatrixB;

    @Setup(Level.Trial)
    public void setUp() {
        if (sparsityLevel == 0.0) {
            denseMatrixA = MatrixGenerator.generateDenseMatrix(matrixSize);
            denseMatrixB = MatrixGenerator.generateDenseMatrix(matrixSize);
            sparseMatrixA = null;
            sparseMatrixB = null;
        } else {
            denseMatrixA = MatrixGenerator.generateSparseMatrixAsDense(matrixSize, sparsityLevel);
            denseMatrixB = MatrixGenerator.generateSparseMatrixAsDense(matrixSize, sparsityLevel);
            sparseMatrixA = MatrixGenerator.generateSparseMatrix(matrixSize, sparsityLevel);
            sparseMatrixB = MatrixGenerator.generateSparseMatrix(matrixSize, sparsityLevel);
        }
    }

    @Benchmark
    public double[][] benchmarkBasicMultiplication() {
        return BasicMultiplication.basicMultiply(denseMatrixA, denseMatrixB);
    }

    @Benchmark
    public double[][] benchmarkCacheBlockedMultiplication() {
        int blockSize = calculateBlockSize(matrixSize);
        return CacheBlockedMultiplication.cacheBlockedMultiply(denseMatrixA, denseMatrixB, blockSize);
    }

    @Benchmark
    public double[][] benchmarkLoopUnrollingMultiplication() {
        return LoopUnrollingMultiplication.loopUnrolledMultiply(denseMatrixA, denseMatrixB);
    }

    @Benchmark
    public double[][] benchmarkParallelMultiplication() {
        return ParallelMultiplication.parallelMultiply(denseMatrixA, denseMatrixB, 4);
    }

    @Benchmark
    public double[][] benchmarkStrassenMultiplication() {
        return StrassenAlgorithm.strassenMultiply(denseMatrixA, denseMatrixB);
    }

    @Benchmark
    public double[][] benchmarkVectorizedMultiplication() {
        return VectorizedMultiplication.vectorizedMultiply(denseMatrixA, denseMatrixB);
    }

    @Benchmark
    public double[][] benchmarkWinogradMultiplication() {
        return WinogradAlgorithm.multiply(denseMatrixA, denseMatrixB);
    }

    @Benchmark
    public double[][] benchmarkSparseMultiplication() {
        if (sparseMatrixA != null && sparseMatrixB != null) {
            return SparseMultiplication.sparseMultiply(sparseMatrixA, sparseMatrixB);
        } else {
            return new double[0][0];
        }
    }

    private int calculateBlockSize(int matrixSize) {
        return switch (matrixSize) {
            case 64, 128 -> 8;
            case 256, 512 -> 32;
            case 1024 -> 64;
            default -> 128;
        };
    }
}
