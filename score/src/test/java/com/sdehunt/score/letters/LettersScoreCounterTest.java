package com.sdehunt.score.letters;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

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
        List<String> inputWords = new ArrayList<>();
        inputWords.add("better");
        inputWords.add("hire");
        inputWords.add("me");

        List<String> solutionWords = new ArrayList<>();
        solutionWords.add("hire");
        solutionWords.add("me");

        long count = lettersScoreCounter.count(UUID.randomUUID().toString(), UUID.randomUUID().toString(), inputLetters, inputWords, solutionWords);
        Assert.assertEquals(6, count);
    }
}
