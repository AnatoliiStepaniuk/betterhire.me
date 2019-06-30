package com.sdehunt.repository;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.BestSolution;
import com.sdehunt.commons.model.BestTaskResult;
import com.sdehunt.commons.model.BestUserResult;

import java.util.Collections;
import java.util.List;

public interface BestSolutionRepository {

    List<BestSolution> getForTask(TaskID taskID, boolean test);

    List<BestSolution> getForUser(String userId, boolean test);

    void save(List<BestSolution> bestSolutions);

    default void save(BestSolution bestSolution) {
        save(Collections.singletonList(bestSolution));
    }

    default List<BestSolution> getForTask(TaskID taskID) {
        return getForTask(taskID, false);
    }

    default List<BestSolution> getForUser(String userId) {
        return getForUser(userId, false);
    }

    /**
     * Returns each users best results for specified task
     */
    List<BestTaskResult> bestTaskResults(String taskId, boolean test);

    /**
     * Returns users best results for all solved tasks
     */
    List<BestUserResult> bestUserResults(String userId, boolean test);
}
