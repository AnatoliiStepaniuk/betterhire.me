package com.sdehunt.repository;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.BestSolution;

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

}
