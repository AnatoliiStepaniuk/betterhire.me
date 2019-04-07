package com.sdehunt.score;

import com.sdehunt.commons.TaskID;
import com.sdehunt.score.slides.SlidesScoreCounter;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts score of solution for specified task
 */
public class GeneralScoreCounterImpl {

    private static Map<TaskID, TaskScoreCounter> taskCounters = new HashMap<>();

    static {
        taskCounters.put(TaskID.SLIDES, new SlidesScoreCounter());
    }

    public long count(TaskID taskId, String repo, String commit) {
        return taskCounters.get(taskId).count(repo, commit);
    }
}
