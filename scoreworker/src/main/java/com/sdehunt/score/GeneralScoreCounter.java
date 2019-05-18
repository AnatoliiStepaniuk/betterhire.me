package com.sdehunt.score;

import com.sdehunt.commons.JavaGithubClient;
import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.score.slides.SlidesScoreCounter;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts score of solution for specified task
 */
public class GeneralScoreCounter {

    private static Map<TaskID, TaskScoreCounter> taskCounters = new HashMap<>();
    private static JavaGithubClient githubClient = new JavaGithubClient();
    static {
        taskCounters.put(TaskID.SLIDES, new SlidesScoreCounter(githubClient));
    }

    public long count(Solution solution) {
        return taskCounters.get(solution.getTaskId()).count(solution.getRepo(), solution.getCommit());
    }
}
