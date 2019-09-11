package com.sdehunt.score.letters;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LettersReader {

    private static String letters = "abcdefghijklmnopqrstuvwxyz";

    @SneakyThrows
    public Map<Character, Integer> readLetters(String file) {

        Map<Character, Integer> lettersMap = emptyLettersMap();

        for (String c : Files.readAllLines( Paths.get(file))) {
            char ch = c.charAt(0);
            lettersMap.put(ch, lettersMap.get(ch) + 1);
        }

        return lettersMap;
    }

    private Map<Character, Integer> emptyLettersMap() {
        Map<Character, Integer> lettersMap = new HashMap<>();
        for (char ch : letters.toCharArray()) {
            lettersMap.put(ch, 0);
        }
        return lettersMap;
    }

}
