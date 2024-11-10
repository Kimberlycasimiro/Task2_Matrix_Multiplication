package org.ulpgc.utils;

import org.ulpgc.sparse.structures.SparseMatrixCSR;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MatrixLoader {
    public static SparseMatrixCSR loadMatrixFromMTX(InputStream inputStream) throws IOException {
        List<Integer> rowPtrList = new ArrayList<>();
        List<Integer> colIndexList = new ArrayList<>();
        List<Double> valuesList = new ArrayList<>();

        int numRows = 0, numCols = 0;
        boolean headerSkipped = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("%")) continue;

                String[] parts = line.trim().split("\\s+");
                if (!headerSkipped) {
                    numRows = Integer.parseInt(parts[0]);
                    numCols = Integer.parseInt(parts[1]);
                    headerSkipped = true;
                } else {
                    int row = Integer.parseInt(parts[0]) - 1;
                    int col = Integer.parseInt(parts[1]) - 1;
                    double value = Double.parseDouble(parts[2]);

                    while (rowPtrList.size() <= row) {
                        rowPtrList.add(valuesList.size());
                    }
                    colIndexList.add(col);
                    valuesList.add(value);
                }
            }
            rowPtrList.add(valuesList.size());
        }

        int[] rowPtr = rowPtrList.stream().mapToInt(i -> i).toArray();
        int[] colIndex = colIndexList.stream().mapToInt(i -> i).toArray();
        double[] values = valuesList.stream().mapToDouble(d -> d).toArray();

        return new SparseMatrixCSR(rowPtr, colIndex, values, numRows, numCols);
    }
}

