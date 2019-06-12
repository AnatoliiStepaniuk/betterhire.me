package com.sdehunt.score.pizza;

import com.sdehunt.commons.exception.InvalidSolutionException;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PizzaReaderImpl implements PizzaReader {
    @Override
    @SneakyThrows(IOException.class)
    public Pizza read(String fileName) {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        String[] info = lines.get(0).split(" ");
        int rows = Integer.valueOf(info[0]);
        int cols = Integer.valueOf(info[1]);
        int minOfEach = Integer.valueOf(info[2]);
        int maxSliceArea = Integer.valueOf(info[3]);
        if (lines.size() - 1 != rows) {
            throw new InvalidSolutionException();
        }

        Ingredient[][] slots = new Ingredient[rows][cols];

        for (int r = 1; r < rows + 1; r++) {
            String line = lines.get(r);
            if (line.length() != cols) {
                throw new RuntimeException();
            }
            for (int c = 0; c < cols; c++) {
                slots[r - 1][c] = Ingredient.of(line.charAt(c));
            }
        }

        return new Pizza(slots, minOfEach, maxSliceArea);
    }
}
