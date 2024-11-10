package org.ulpgc.sparse.structures;

public class SparseMatrixCSC {
    private final int[] colPtr;
    private final int[] rowIndex;
    private final double[] values;
    private final int numRows;
    private final int numCols;

    public SparseMatrixCSC(int[] colPtr, int[] rowIndex, double[] values, int numRows, int numCols) {
        this.colPtr = colPtr;
        this.rowIndex = rowIndex;
        this.values = values;
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public int[] getColPtr() {
        return colPtr;
    }

    public int[] getRowIndex() {
        return rowIndex;
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
}
