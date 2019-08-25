package com.sdehunt.score;

import com.sdehunt.commons.TaskID;
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

    private Map<TaskID, TaskScoreCounter> taskCounters = new HashMap<>();

    public GeneralScoreCounter(GeneralFilesDownloader filesDownloader) {
        taskCounters.put(TaskID.SLIDES, new SlidesScoreCounter(filesDownloader));
        taskCounters.put(TaskID.SLIDES_TEST, SlidesScoreCounter.test(filesDownloader));
        taskCounters.put(TaskID.PIZZA, new PizzaScoreCounter(filesDownloader));
        taskCounters.put(TaskID.CARS, new CarsScoreCounter(filesDownloader));
        taskCounters.put(TaskID.LETTERS, new GeneralLettersScoreCounter(filesDownloader));
        taskCounters.put(TaskID.CITIES, new CitiesScoreCounter(filesDownloader));
        taskCounters.put(TaskID.LETTER, new GeneralLettersScoreCounter(filesDownloader));
        taskCounters.put(TaskID.CITY, new CitiesScoreCounter(filesDownloader));
    }

    public long count(Solution solution) throws CommitOrFileNotFoundException {
        return taskCounters.get(solution.getTaskId()).count(solution.getUserId(), solution.getRepo(), solution.getCommit());
    }
}
