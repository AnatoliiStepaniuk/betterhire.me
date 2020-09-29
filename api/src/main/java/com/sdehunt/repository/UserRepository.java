package com.sdehunt.repository;

import com.sdehunt.commons.model.User;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * Describes operations with User entities
 */
public interface UserRepository {

    User create(User user);

    Optional<User> get(String userId);

    Collection<User> getByIds(Collection<String> userIds);

    default Optional<User> find(User user) {
        Optional<User> found = Optional.empty();

        if (!StringUtils.isEmpty(user.getEmail())) {
            found = byEmail(user.getEmail());
        }

        if (!found.isPresent()) {
            found = byGithubLogin(user.getGithubLogin());
        }

        if (!found.isPresent()) {
            found = byLinkedinId(user.getLinkedinId());
        }

        return found;
    }

    Optional<User> byEmail(String email);

    Optional<User> byGithubLogin(String githubLogin);

    Optional<User> byLinkedinId(String linkedinId);

    User update(User user);

    void delete(String userId);

    Collection<User> getAll(boolean test);

    Collection<User> query(UserQuery userQuery);

    Collection<User> getBySql(String sql);

    /**
     * @return Total number of users that solved at least one task successfully
     */
    long getTotalUsers();

    /**
     * @return Number of Active users in specified time range.
     * Active - those who solved at least one task (successfully or not).
     */
    long getActiveUsersInRange(Instant from, Instant to);
}
