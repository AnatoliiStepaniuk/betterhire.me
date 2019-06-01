package com.sdehunt.repository;

import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Task;

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
     * Returns all present tasks (short versions)
     */
    List<ShortTask> getAllShort();

    /**
     * Returns task for provided id if found
     */
    Optional<Task> get(String id);

    /**
     * Returns short task version for provided id if found
     */
    Optional<ShortTask> getShort(String id);

    /**
     * Deletes task with provided id
     */
    void delete(String id);

    /**
     * Updates tasks
     */
    void update(Task task);

}
