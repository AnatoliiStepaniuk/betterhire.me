package com.sdehunt.score.cars;

public class DistanceCounter {

    public static int dist(Point from, Point to) {
        return Math.abs(from.getRow() - to.getRow()) + Math.abs(from.getCol() - to.getCol());
    }

}
