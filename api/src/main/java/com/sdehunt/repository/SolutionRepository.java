package com.sdehunt.repository;

import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.repository.impl.SolutionQueryImpl;

import java.util.*;

/**
 * Repository for managing Solution entities persistence
 */
public interface SolutionRepository {

    /**
     * Saves Solution and returns assigned id.
     */
    String save(Solution solution);

    /**
     * Updates Solution.
     */
    void update(Solution solution);

    /**
     * Gets Solution by provided id if found.
     */
    Optional<Solution> get(String id);

    /**
     * Deletes Solution by provided id.
     */
    void delete(String id);

    /**
     * Returns Solutions for specified query
     */
    List<Solution> query(SolutionQuery request);

    /**
     * Checks if solution with this repo+commit is already present for specified user
     */
    boolean isPresentForUser(Solution solution);

    /**
     * @return Total number of successful solutions
     */
    long getTotalSolutions();

    /**
     * Returns Solutions of specified user
     */
    default List<Solution> forUser(String userId) {
        return forUser(userId, false);
    }

    default List<Solution> forUser(String userId, boolean test) {
        return query(new SolutionQueryImpl().user(userId).test(test));
    }

    /**
     * Returns all repositories mapped by userId
     */
    Map<String, List<String>> getAllRepos();

    /**
     * Returns number of users that submitted solution for the task (any status).
     */
    default int getNumberUsersSolvedTask(String taskId) {
        return getNumberUsersSolvedTask(taskId, Collections.emptySet());
    }

    Map<String, List<String>> getTasksRepos(Set<String> taskIds);

    /**
     * Returns number of users that submitted solution for the task in specified statuses.
     */
    int getNumberUsersSolvedTask(String taskId, Set<SolutionStatus> statuses);

    /**
     * Returns userIds that solved specified task ids
     */
    Set<String> solvedTasks(Set<String> taskIds);
}
