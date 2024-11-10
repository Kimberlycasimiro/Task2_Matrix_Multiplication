package org.ulpgc.sparse.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixMultiplicationCSRCSC {
    public static SparseMatrixCSR multiply(SparseMatrixCSR matrixA, SparseMatrixCSC matrixB) {
        int numRows = matrixA.getNumRows();
        int numCols = matrixB.getNumCols();

        int[] rowPtrResult = new int[numRows + 1];
        List<Integer> colIndexResult = new ArrayList<>();
        List<Double> valuesResult = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            Map<Integer, Double> rowResult = new HashMap<>();

            for (int j = matrixA.getRowPtr()[i]; j < matrixA.getRowPtr()[i + 1]; j++) {
                int colA = matrixA.getColIndex()[j];
                double valueA = matrixA.getValues()[j];

                for (int k = matrixB.getColPtr()[colA]; k < matrixB.getColPtr()[colA + 1]; k++) {
                    int rowB = matrixB.getRowIndex()[k];
                    double valueB = matrixB.getValues()[k];

                    rowResult.put(rowB, rowResult.getOrDefault(rowB, 0.0) + valueA * valueB);
                }
            }

            rowPtrResult[i + 1] = rowPtrResult[i] + rowResult.size();
            for (Map.Entry<Integer, Double> entry : rowResult.entrySet()) {
                colIndexResult.add(entry.getKey());
                valuesResult.add(entry.getValue());
            }
        }

        int[] colIndexArray = colIndexResult.stream().mapToInt(Integer::intValue).toArray();
        double[] valuesArray = valuesResult.stream().mapToDouble(Double::doubleValue).toArray();

        return new SparseMatrixCSR(rowPtrResult, colIndexArray, valuesArray, numRows, numCols);
    }
}
