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
    default List<Task> getAll() {
        return getAll(false);
    }

    List<Task> getAll(boolean test);

    /**
     * Returns all present tasks (short versions)
     */
    default List<ShortTask> getAllShort() {
        return getAllShort(false);
    }

    List<ShortTask> getAllShort(boolean test);

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
    Task update(Task task);

    /**
     * Creates tasks
     */
    Task create(Task task);

    /**
     * Returns history of task changes.
     */
    List<Task> getHistory(String taskId);

    List<Task> getForCompany(String company, boolean enabledOnly);

    default List<Task> getForCompany(String company) {
        return getForCompany(company, true);
    }

}
