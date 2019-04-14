package com.sdehunt.repository;

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
     * Returns Solutions of specified user
     */
    default List<Solution> forUser(String userId) {
        return query(new SolutionQueryImpl().withUser(userId));
    }
}
