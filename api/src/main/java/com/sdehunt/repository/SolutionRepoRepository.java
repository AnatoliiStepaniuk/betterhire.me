package com.sdehunt.repository;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.SolutionRepo;

import java.util.Optional;

/**
 * Repository for user managing users solution repositories.
 */
public interface SolutionRepoRepository {

    /**
     * Returns repository for task and userId if present
     */
    Optional<SolutionRepo> find(TaskID taskId, String userId);

    /**
     * Creates new solution repo entry.
     */
    void save(TaskID taskID, String userId, String repo, String webhookSecret);

}
