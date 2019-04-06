package com.sdehunt.repository;

import com.sdehunt.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Describes operations with Task entities
 */
public interface TaskRepository {

    /**
     * Returns all present tasks
     */
    List<Task> getAll();

    /**
     * Returns task for provided id if found
     */
    Optional<Task> get(String id);

    /**
     * Deletes task with provided id
     */
    void delete(String id);

    /**
     * Create tasks and returns its id
     */
    String create(Task task);

}
