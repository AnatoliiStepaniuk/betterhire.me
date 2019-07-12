package com.sdehunt.score.pizza;

import com.sdehunt.commons.exception.InvalidSolutionException;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EagerSliceReader implements SliceReader {
    @Override
    @SneakyThrows(IOException.class)
    public List<Slice> read(String fileName) {
        List<Slice> slices = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        if (lines.size() - 1 != Integer.valueOf(lines.get(0))) {
            String cause = "The number of lines in a file did not match integer in first line. Check requirements for output files.";
            throw new InvalidSolutionException(cause);
        }
        for (int i = 1; i < lines.size(); i++) {
            slices.add(new Slice(lines.get(i)));
        }
        return slices;
    }
}
