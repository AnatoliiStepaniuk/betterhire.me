package com.sdehunt.score.letters;

import com.sdehunt.commons.exception.InvalidSolutionException;

import java.util.List;
import java.util.Map;

public class LettersScoreCounter {

    public long count(
            Map<Character, Integer> inputLetters,
            List<String> inputWords,
            List<String> solutionWords
    ) {
        solutionWords.forEach(w -> checkWordPresent(inputWords, w));
        solutionWords.forEach(w -> removeUsedLetters(inputLetters, w));
        return solutionWords.stream().mapToLong(String::length).sum();
    }

    private void checkWordPresent(List<String> words, String word) {
        if (!words.remove(word)) {
            throw new InvalidSolutionException();
        }
    }

    private void removeUsedLetters(Map<Character, Integer> letters, String word) {
        for (char ch : word.toCharArray()) {
            letters.put(ch, letters.get(ch) - 1);
            if (letters.get(ch) < 0) {
                throw new InvalidSolutionException();
            }
        }
    }

}
