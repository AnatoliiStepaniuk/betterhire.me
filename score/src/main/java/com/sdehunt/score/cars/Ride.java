package com.sdehunt.score.cars;

import lombok.Data;

@Data
public class Ride {
    private Point from;
    private Point to;

    private int start;
    private int finish;
}
