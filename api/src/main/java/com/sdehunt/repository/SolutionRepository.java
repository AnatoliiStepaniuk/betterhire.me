package com.sdehunt.repository;

import com.sdehunt.model.Solution;

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
}
