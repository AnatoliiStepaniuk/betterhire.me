package com.sdehunt.score.slides;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            List<String> lines = Files.readAllLines(Paths.get(fileName)); // TODO make method that accepts lines and create a test for it
            for(int i =1; i < lines.size(); i++){
                pictures.put(i-1, toPicture(lines.get(i)));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pictures;
    }
}
