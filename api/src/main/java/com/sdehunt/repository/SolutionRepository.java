package com.sdehunt.repository;

import com.sdehunt.commons.model.BestResult;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.repository.impl.SolutionQueryImpl;

import java.util.List;
import java.util.Optional;

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
     * Returns each users best results for specified task
     */
    List<BestResult> best(String taskId);

    /**
     * Checks if solution with this repo+commit is already present for specified user
     */
    boolean isPresentForUser(Solution solution);

    /**
     * Returns Solutions of specified user
     */
    default List<Solution> forUser(String userId) {
        return forUser(userId, false);
    }

    default List<Solution> forUser(String userId, boolean test) {
        return query(new SolutionQueryImpl().user(userId).test(test));
    }

}
