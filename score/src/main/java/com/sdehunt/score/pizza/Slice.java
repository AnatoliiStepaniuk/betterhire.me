package com.sdehunt.score.pizza;

import lombok.Getter;

public class Slice {
    @Getter
    private int startRow;
    @Getter
    private int startCol;
    @Getter
    private int endRow;
    @Getter
    private int endCol;

    public Slice(String str) {
        String[] data = str.split(" ");
        this.startRow = Integer.valueOf(data[0]);
        this.startCol = Integer.valueOf(data[1]);
        this.endRow = Integer.valueOf(data[2]);
        this.endCol = Integer.valueOf(data[3]);
    }

    public Slice(int startRow, int startCol, int endRow, int endCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    public int area() {
        return rows() * cols();
    }

    public int rows() {
        return (endRow - startRow + 1);
    }

    public int cols() {
        return (endCol - startCol + 1);
    }

}
