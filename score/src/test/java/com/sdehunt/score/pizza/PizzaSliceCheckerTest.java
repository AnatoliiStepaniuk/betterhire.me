package com.sdehunt.score.pizza;

import com.sdehunt.commons.exception.InvalidSolutionException;
import org.junit.Test;

import static com.sdehunt.score.pizza.Ingredient.*;

public class PizzaSliceCheckerTest {

    private static final Ingredient[][] slots = new Ingredient[][]{
            {TOMATO, TOMATO, TOMATO, TOMATO, TOMATO},
            {TOMATO, MUSHROOM, MUSHROOM, MUSHROOM, TOMATO},
            {TOMATO, TOMATO, TOMATO, TOMATO, TOMATO}

    };
    private static final Pizza pizza = new Pizza(slots, 1, 6);

    private static final Ingredient[][] slotsWithEmpties = new Ingredient[][]{
            {TOMATO, TOMATO, TOMATO, TOMATO, TOMATO},
            {EMPTY, EMPTY, MUSHROOM, MUSHROOM, TOMATO},
            {EMPTY, EMPTY, TOMATO, TOMATO, TOMATO}

    };
    private static final Pizza pizzaWithEmpties = new Pizza(slotsWithEmpties, 1, 6);

    @Test
    public void checkFitsSuccessTest() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 0, 2, 4);
        checker.checkFits(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkFitsFail1Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(-1, 0, 2, 4);
        checker.checkFits(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkFitsFail2Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, -1, 2, 4);
        checker.checkFits(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkFitsFail3Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 0, 3, 4);
        checker.checkFits(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkFitsFail4Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 0, 2, 5);
        checker.checkFits(pizza, slice);
    }

    @Test
    public void checkMinSuccessTest() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice1 = new Slice(0, 0, 1, 1);
        checker.checkMin(pizza, slice1);
        Slice slice2 = new Slice(0, 2, 2, 3);
        checker.checkMin(pizza, slice2);
        Slice slice3 = new Slice(0, 3, 1, 4);
        checker.checkMin(pizza, slice3);
        Slice slice4 = new Slice(1, 3, 1, 4);
        checker.checkMin(pizza, slice4);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkMinFail1Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 0, 0, 4);
        checker.checkMin(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkMinFail2Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(1, 1, 1, 3);
        checker.checkMin(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkMinFail3Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 4, 2, 4);
        checker.checkMin(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkMinFail4Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(1, 2, 1, 2);
        checker.checkMin(pizza, slice);
    }

    @Test
    public void checkMaxAreaSuccessTest() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice1 = new Slice(0, 0, 0, 1);
        checker.checkMaxArea(pizza, slice1);
        Slice slice2 = new Slice(0, 1, 1, 2);
        checker.checkMaxArea(pizza, slice2);
        Slice slice3 = new Slice(0, 3, 2, 4);
        checker.checkMaxArea(pizza, slice3);
        Slice slice4 = new Slice(1, 1, 2, 3);
        checker.checkMaxArea(pizza, slice4);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkMaxAreaFail1Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 0, 1, 3);
        checker.checkMaxArea(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkMaxAreaFail2Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 2, 2, 4);
        checker.checkMaxArea(pizza, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkMaxAreaFail3Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 0, 2, 3);
        checker.checkMaxArea(pizza, slice);
    }

    @Test
    public void checkSlotsSuccessTest() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice1 = new Slice(0, 0, 2, 4);
        checker.checkSlots(pizza, slice1);
        Slice slice2 = new Slice(0, 0, 0, 4);
        checker.checkSlots(pizza, slice2);
        Slice slice3 = new Slice(0, 2, 2, 4);
        checker.checkSlots(pizza, slice3);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkSlotsFail1Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 0, 1, 2);
        checker.checkSlots(pizzaWithEmpties, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkSlotsFail2Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(0, 1, 2, 2);
        checker.checkSlots(pizzaWithEmpties, slice);
    }

    @Test(expected = InvalidSolutionException.class)
    public void checkSlotsFail3Test() {
        PizzaSliceChecker checker = new PizzaSliceChecker();
        Slice slice = new Slice(2, 0, 2, 4);
        checker.checkSlots(pizzaWithEmpties, slice);
    }

}
