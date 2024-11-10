package org.ulpgc.benchmark.sparse;

import org.openjdk.jmh.annotations.*;
import org.ulpgc.sparse.optimization.SparseMultiplication;
import org.ulpgc.sparse.optimization.SparseMatrix;
import org.ulpgc.utils.MatrixGenerator;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class SparseMatrixMultiplicationBenchmark {

    @Param({"64", "128", "256", "512", "1024", "2048"})
    public int matrixSize;

    @Param({"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9"})
    public double sparsityLevel;

    private SparseMatrix sparseMatrixA;
    private SparseMatrix sparseMatrixB;

    @Setup(Level.Trial)
    public void setUp() {
        sparseMatrixA = MatrixGenerator.generateSparseMatrix(matrixSize, sparsityLevel);
        sparseMatrixB = MatrixGenerator.generateSparseMatrix(matrixSize, sparsityLevel);
    }

    @Benchmark
    public double[][] benchmarkSparseMultiplication() {
        return SparseMultiplication.sparseMultiply(sparseMatrixA, sparseMatrixB);
    }
}