package com.sdehunt.repository;

import java.util.Optional;

/**
 * Represents general query for Solution entities
 */
public interface SolutionQuery {

    Optional<String> getUser();

    Optional<String> getTask();

}
