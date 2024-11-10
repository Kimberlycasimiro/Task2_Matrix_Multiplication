package org.ulpgc.sparse.structures;

public class SparseMatrixCSR {
    private final int[] rowPtr;
    private final int[] colIndex;
    private final double[] values;
    private final int numRows;
    private final int numCols;

    public SparseMatrixCSR(int[] rowPtr, int[] colIndex, double[] values, int numRows, int numCols) {
        this.rowPtr = rowPtr;
        this.colIndex = colIndex;
        this.values = values;
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public int[] getRowPtr() {
        return rowPtr;
    }

    public int[] getColIndex() {
        return colIndex;
    }

    public double[] getValues() {
        return values;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public SparseMatrixCSC toCSC() {
        int[] colPtr = new int[numCols + 1];
        int[] rowIndex = new int[values.length];
        double[] valuesCSC = new double[values.length];

        for (int col : colIndex) {
            colPtr[col + 1]++;
        }

        for (int i = 1; i <= numCols; i++) {
            colPtr[i] += colPtr[i - 1];
        }

        for (int i = 0; i < numRows; i++) {
            for (int j = rowPtr[i]; j < rowPtr[i + 1]; j++) {
                int col = colIndex[j];
                int destPos = colPtr[col];

                rowIndex[destPos] = i;
                valuesCSC[destPos] = values[j];
                colPtr[col]++;
            }
        }

        for (int i = numCols; i > 0; i--) {
            colPtr[i] = colPtr[i - 1];
        }
        colPtr[0] = 0;

        return new SparseMatrixCSC(colPtr, rowIndex, valuesCSC, numRows, numCols);
    }
}

