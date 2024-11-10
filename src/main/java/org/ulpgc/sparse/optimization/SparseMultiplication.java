package org.ulpgc.sparse.optimization;

public class SparseMultiplication {

    public static double[][] sparseMultiply(SparseMatrix a, SparseMatrix b) {
        int size = a.getSize();
        double[][] result = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = a.getRowPointers().get(i); j < a.getRowPointers().get(i + 1); j++) {
                int colA = a.getColIndices().get(j);
                double valueA = a.getValues().get(j);

                for (int k = b.getRowPointers().get(colA); k < b.getRowPointers().get(colA + 1); k++) {
                    int colB = b.getColIndices().get(k);
                    result[i][colB] += valueA * b.getValues().get(k);
                }
            }
        }

        return result;
    }
}
