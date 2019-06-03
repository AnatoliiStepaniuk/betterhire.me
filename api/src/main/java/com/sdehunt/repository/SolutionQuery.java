package com.sdehunt.repository;

import com.sdehunt.commons.model.SolutionStatus;

import java.util.Optional;

/**
 * Represents general query for Solution entities
 */
public interface SolutionQuery {

    Optional<String> getUser();

    Optional<String> getTask();

    Optional<SolutionStatus> getStatus();
}
