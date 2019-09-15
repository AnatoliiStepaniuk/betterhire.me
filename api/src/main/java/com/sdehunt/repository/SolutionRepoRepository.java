package com.sdehunt.repository;

import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.SolutionRepo;

import java.util.Optional;

/**
 * Repository for user managing users solution repositories.
 */
public interface SolutionRepoRepository {

    /**
     * Returns repository for task and userId and language if present
     */
    Optional<SolutionRepo> find(String taskId, String userId, Language language);

    /**
     * Returns repository by name
     */
    Optional<SolutionRepo> find(String repo);

    /**
     * Creates new solution repo entry.
     */
    void save(String taskId, String userId, Language language, String repo, String webhookSecret);

}
