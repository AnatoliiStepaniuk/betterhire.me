package com.sdehunt.score.slides;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Uses non-lazy method Files.readAllLines()
 */
public class EagerPicturesReader implements PicturesReader{

    @Override
    public Map<Integer, Picture> readPictures(String fileName) {
            Map<Integer, Picture> pictures = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for(int i =1; i < lines.size(); i++){
                pictures.put(i-1, toPicture(lines.get(i)));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pictures;
    }

    private Picture toPicture(String line){
        String[] split = line.split(" ");
        return new Picture(
                split[0] == "H",
                Arrays.asList(Arrays.copyOfRange(split, 2, split.length))
        );
    }
}
