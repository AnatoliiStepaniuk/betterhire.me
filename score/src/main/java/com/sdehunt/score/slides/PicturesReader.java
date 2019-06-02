package com.sdehunt.score.slides;

import java.util.Arrays;
import java.util.Map;

public interface PicturesReader {

    Map<Integer, Picture> readPictures(String fileName);

    default Picture toPicture(String line) { // TODO test for this method
        String[] split = line.split(" ");
        return new Picture(
                isVertical(line),
                Arrays.asList(Arrays.copyOfRange(split, 2, split.length))
        );
    }

    default boolean isVertical(String line) {
        String first = line.split(" ")[0];
        if (first.equals("H")) {
            return false;
        }
        if (first.equals("V")) {
            return true;
        }
        throw new RuntimeException("Line should start with either H or V but was - " + first);
    }
}
