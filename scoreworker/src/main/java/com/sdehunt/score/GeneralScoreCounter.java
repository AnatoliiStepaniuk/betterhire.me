package com.sdehunt.score;

import com.sdehunt.commons.TaskID;
import com.sdehunt.score.slides.SlidesScoreCounter;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts score of solution for specified task
 */
public class GeneralScoreCounter {

    private static Map<TaskID, TaskScoreCounter> taskCounters = new HashMap<>();

    static {
        taskCounters.put(TaskID.SLIDES, new SlidesScoreCounter());
    }

    public long count(TaskID taskId, String repo, String commit) { // TODO accept solution object
        return taskCounters.get(taskId).count(repo, commit);
    }
}
