package com.sdehunt.score.pizza;

import com.sdehunt.commons.exception.InvalidSolutionException;

public class PizzaSliceChecker {

    public void check(Pizza pizza, Slice slice) {
        checkFits(pizza, slice);
        checkMin(pizza, slice);
        checkMaxArea(pizza, slice);
        checkSlots(pizza, slice);
    }

    void checkFits(Pizza p, Slice s) {
        if (s.getStartRow() < 0 || s.getStartCol() < 0 || s.getEndRow() > p.rows() - 1 || s.getEndCol() > p.cols() - 1) {
            String cause = "Some slices do not fit the pizza boundaries";
            throw new InvalidSolutionException(cause);
        }
    }

    void checkMin(Pizza pizza, Slice s) {
        int tomatoCount = 0;
        int mushroomCount = 0;
        for (int r = s.getStartRow(); r <= s.getEndRow(); r++) {
            for (int c = s.getStartCol(); c <= s.getEndCol(); c++) {
                if (pizza.getSlots()[r][c] == Ingredient.MUSHROOM) {
                    mushroomCount++;
                }
                if (pizza.getSlots()[r][c] == Ingredient.TOMATO) {
                    tomatoCount++;
                }

                if (tomatoCount >= pizza.getMinOfEach() && mushroomCount >= pizza.getMinOfEach()) {
                    return;
                }
            }
        }
        String cause = "Some slices did not contain minimum number of each ingredient.";
        throw new InvalidSolutionException(cause);
    }

    void checkMaxArea(Pizza pizza, Slice slice) {
        if (slice.area() > pizza.getMaxSliceArea()) {
            String cause = "Some slices were bigger that allowed. Check maximum allowed area.";
            throw new InvalidSolutionException(cause);
        }
    }

    void checkSlots(Pizza p, Slice s) {
        for (int r = s.getStartRow(); r <= s.getEndRow(); r++) {
            for (int c = s.getStartCol(); c <= s.getEndCol(); c++) {
                if (p.getSlots()[r][c] == Ingredient.EMPTY) {
                    String cause = "Some slices overlap with each other.";
                    throw new InvalidSolutionException(cause);
                }
            }
        }
    }
}
