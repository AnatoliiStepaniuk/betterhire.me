package com.sdehunt.score.cars;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConditionsReader {

    @SneakyThrows(IOException.class)
    public Conditions read(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // TODO use this in other counters
            String[] conditions = reader.readLine().split(" ");
            return new Conditions()
                    .setRows(Integer.valueOf(conditions[0]))
                    .setCols(Integer.valueOf(conditions[1]))
                    .setVehicles(Integer.valueOf(conditions[2]))
                    .setRides(Integer.valueOf(conditions[3]))
                    .setBonus(Integer.valueOf(conditions[4]))
                    .setSteps(Integer.valueOf(conditions[5]));
        }
    }

}
