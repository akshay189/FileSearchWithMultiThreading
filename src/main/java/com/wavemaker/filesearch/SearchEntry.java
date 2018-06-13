package com.wavemaker.filesearch;

public class SearchEntry {
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

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof SearchEntry) {
                if (((SearchEntry) obj).getRowNumber() == this.rowNumber && ((SearchEntry) obj).getColumnNumber() == this.columnNumber) {
                    return true;
                }
            }
        }
        return false;
    }
}
