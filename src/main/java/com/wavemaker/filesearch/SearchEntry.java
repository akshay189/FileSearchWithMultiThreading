package com.wavemaker.filesearch;

public class SearchEntry
{
    private int rowNumber;
    private int columnNumber;

    public SearchEntry(int rowNumber, int columnNumber) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
