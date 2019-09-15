package com.sdehunt.score;

import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.score.cars.CarsScoreCounter;
import com.sdehunt.score.cities.CitiesScoreCounter;
import com.sdehunt.score.letters.GeneralLettersScoreCounter;
import com.sdehunt.score.pizza.PizzaScoreCounter;
import com.sdehunt.score.slides.SlidesScoreCounter;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts score of solution for specified task
 */
public class GeneralScoreCounter {

    private Map<String, TaskScoreCounter> taskCounters = new HashMap<>();

    public GeneralScoreCounter(GeneralFilesDownloader filesDownloader) {
        taskCounters.put("slides", new SlidesScoreCounter(filesDownloader));
        taskCounters.put("slides_test", SlidesScoreCounter.test(filesDownloader));
        taskCounters.put("pizza", new PizzaScoreCounter(filesDownloader));
        taskCounters.put("cars", new CarsScoreCounter(filesDownloader));
        taskCounters.put("letters", new GeneralLettersScoreCounter(filesDownloader));
        taskCounters.put("cities", new CitiesScoreCounter(filesDownloader));
        taskCounters.put("letter", new GeneralLettersScoreCounter(filesDownloader));
        taskCounters.put("city", new CitiesScoreCounter(filesDownloader));
    }

    public long count(Solution solution) throws CommitOrFileNotFoundException {
        return taskCounters.get(solution.getTaskId()).count(solution.getUserId(), solution.getRepo(), solution.getCommit());
    }
}
