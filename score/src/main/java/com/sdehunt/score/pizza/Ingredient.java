package com.sdehunt.score.pizza;

public enum Ingredient {
    MUSHROOM,
    TOMATO,
    EMPTY;

    public static Ingredient of(char c) {
        return of(String.valueOf(c));
    }

    public static Ingredient of(String s) {
        switch (s.toLowerCase()) {
            case "m":
                return MUSHROOM;
            case "t":
                return TOMATO;
            default:
                throw new RuntimeException();
        }
    }
}
