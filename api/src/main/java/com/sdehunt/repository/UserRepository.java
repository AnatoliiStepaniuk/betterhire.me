package com.sdehunt.repository;

import com.sdehunt.commons.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Describes operations with User entities
 */
public interface UserRepository {

    User create(User user);

    Optional<User> get(String userId);

    User update(User user);

    void delete(String userId);

    Collection<User> getAll(); // TODO use pagination in future
}
