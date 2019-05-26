package com.sdehunt.score;

import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;

/**
 * Counts score of solution (for the task it's dedicated to)
 */
public interface TaskScoreCounter {

    long count(String repo, String commit) throws CommitOrFileNotFoundException;

}
