package org.ulpgc.sparse.optimization;

import java.util.ArrayList;
import java.util.List;

public class SparseMatrix {
    private final int size;
    private final List<Integer> rowPointers;
    private final List<Integer> colIndices;
    private final List<Double> values;

    public SparseMatrix(int size) {
        this.size = size;
        this.rowPointers = new ArrayList<>();
        this.colIndices = new ArrayList<>();
        this.values = new ArrayList<>();
        rowPointers.add(0);
    }

    public int getSize() {
        return size;
    }

    public void addValue(int row, int col, double value) {
        if (value != 0) {
            colIndices.add(col);
            values.add(value);
            while (rowPointers.size() <= row + 1) {
                rowPointers.add(colIndices.size());
            }
        }
    }

    public double getValue(int row, int col) {
        for (int i = rowPointers.get(row); i < rowPointers.get(row + 1); i++) {
            if (colIndices.get(i) == col) {
                return values.get(i);
            }
        }
        return 0.0;
    }

    public List<Integer> getRowPointers() {
        return rowPointers;
    }

    public List<Integer> getColIndices() {
        return colIndices;
    }

    public List<Double> getValues() {
        return values;
    }
}

