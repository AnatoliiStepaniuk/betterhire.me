package com.sdehunt.score.letters;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LettersScoreCounterTest {

    private LettersScoreCounter lettersScoreCounter = new LettersScoreCounter();

    @Test
    public void happyFlow() {
        Map<Character, Integer> inputLetters = new HashMap<>();
        inputLetters.put('b', 0);
        inputLetters.put('e', 3);
        inputLetters.put('t', 0);
        inputLetters.put('r', 1);
        inputLetters.put('h', 2);
        inputLetters.put('i', 2);
        inputLetters.put('m', 2);
        List<String> inputWords = Arrays.asList("better", "hire", "me");
        List<String> solutionWords = Arrays.asList("hire", "me");

        long count = lettersScoreCounter.count(inputLetters, inputWords, solutionWords);
        Assert.assertEquals(6, count);
    }
}
