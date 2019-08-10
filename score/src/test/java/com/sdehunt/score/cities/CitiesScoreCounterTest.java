package com.sdehunt.score.cities;

import com.sdehunt.commons.exception.InvalidSolutionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CitiesScoreCounterTest {

    private final CitiesScoreCounter scoreCounter = new CitiesScoreCounter(null);

    @Test
    public void happyFlowTest() {
        List<String> citiesList = Arrays.asList("Kyiv", "Vilnius", "Stockholm");
        List<String> availableCities = Arrays.asList("Kyiv", "Vilnius", "Stockholm");
        long expectedScore = 20;
        long actualScore = scoreCounter.checkAndCountScore(citiesList, availableCities);

        Assert.assertEquals(expectedScore, actualScore);
    }

    @Test(expected = InvalidSolutionException.class)
    public void wrongOrderTest() {
        List<String> citiesList = Arrays.asList("Vilnius", "Kyiv", "Stockholm");
        List<String> availableCities = Arrays.asList("Kyiv", "Vilnius", "Stockholm");

        scoreCounter.checkAndCountScore(citiesList, availableCities);
    }

    @Test(expected = InvalidSolutionException.class)
    public void notPresentCityTest() {
        List<String> citiesList = Arrays.asList("Kyiv", "Vilnius", "Stockholm");
        List<String> availableCities = Arrays.asList("Kyiv", "Stockholm");

        scoreCounter.checkAndCountScore(citiesList, availableCities);
    }

    @Test(expected = InvalidSolutionException.class)
    public void duplicateCityTest() {
        List<String> citiesList = Arrays.asList("Kyiv", "Vilnius", "San Carlos", "San Carlos");
        List<String> availableCities = Arrays.asList("Kyiv", "Vilnius", "San Carlos");

        scoreCounter.checkAndCountScore(citiesList, availableCities);
    }

}
