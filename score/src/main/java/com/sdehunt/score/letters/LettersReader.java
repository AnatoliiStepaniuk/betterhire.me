package com.sdehunt.score.letters;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class LettersReader {

    private static String letters = "abcdefghijklmnopqrstuvwxyz";

    @SneakyThrows
    public Map<Character, Integer> readLetters(String file) {

        Map<Character, Integer> lettersMap = emptyLettersMap();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        Integer number = Integer.valueOf(reader.readLine());
        for (int i = 0; i < number; i++) {
            char ch = (char) reader.read();
            lettersMap.put(ch, lettersMap.get(ch) + 1);
            if (i != number - 1) {
                reader.read(); // newline
            }
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
