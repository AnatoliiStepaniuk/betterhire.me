package com.sdehunt.score;

/**
 * Counts score of solution (for the task it's dedicated to)
 */
public interface TaskScoreCounter {

    long count(String repo, String commit);

}
