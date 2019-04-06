package com.sdehunt.repository;

import com.sdehunt.model.Solution;

import java.util.List;

/**
 * Repository for managing Solution entities persistence
 */
public interface SolutionRepository {

    /**
     * Saves solution and returns assigned id.
     */
    String save(Solution solution);

    /**
     * Deletes solution by provided id.
     */
    void delete(String id);

    /**
     * Returns solutions for specified query
     */
    List<Solution> query(SolutionQuery request);
}
