package com.sdehunt.score.pizza;

import lombok.Getter;

public class Pizza {
    @Getter
    private Ingredient[][] slots;
    @Getter
    private int minOfEach;
    @Getter
    private int maxSliceArea;

    public Pizza(Ingredient[][] slots, int minOfEach, int maxSliceArea) {
        this.slots = slots;
        this.minOfEach = minOfEach;
        this.maxSliceArea = maxSliceArea;
    }

    public int rows() {
        return slots.length;
    }

    public int cols() {
        return slots[0].length;
    }

    void remove(Slice s) {
        for (int r = s.getStartRow(); r <= s.getEndRow(); r++) {
            for (int c = s.getStartCol(); c <= s.getEndCol(); c++) {
                slots[r][c] = Ingredient.EMPTY;
            }
        }
    }
}
