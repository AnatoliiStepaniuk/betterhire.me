package com.sdehunt.score.letters;

import com.sdehunt.commons.exception.InvalidSolutionException;

import java.util.List;
import java.util.Map;

public class LettersScoreCounter {

    public long count(
            String inputWordsFile,
            String inputLettersFile,
            Map<Character, Integer> inputLetters,
            List<String> inputWords,
            List<String> solutionWords
    ) {
        solutionWords.forEach(w -> checkWordPresent(inputWordsFile, inputWords, w));
        solutionWords.forEach(w -> removeUsedLetters(inputLettersFile, inputLetters, w));
        return solutionWords.stream().mapToLong(String::length).sum();
    }

    private void checkWordPresent(String inputWordsFile, List<String> words, String word) {
        if (!words.remove(word)) {
            String cause = "Word " + word + " is not present in input file " + inputWordsFile + " or present less times than used in output file.";
            throw new InvalidSolutionException(cause);
        }
    }

    private void removeUsedLetters(String inputLettersFile, Map<Character, Integer> letters, String word) {
        for (char ch : word.toCharArray()) {
            letters.put(ch, letters.get(ch) - 1);
            if (letters.get(ch) < 0) {
                String cause = "You used some letters more than was available in input file (Letter: " + ch + " for file: " + inputLettersFile + ")";
                throw new InvalidSolutionException(cause);
            }
        }
    }

}
