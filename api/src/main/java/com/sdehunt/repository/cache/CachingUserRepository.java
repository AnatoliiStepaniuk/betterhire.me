package com.sdehunt.repository.cache;

import com.sdehunt.commons.cache.Cache;
import com.sdehunt.commons.cache.CacheImpl;
import com.sdehunt.commons.model.User;
import com.sdehunt.repository.UserQuery;
import com.sdehunt.repository.UserRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

public class CachingUserRepository implements UserRepository {

    private UserRepository inner;
    private Cache<String, Long> userCache;

    public CachingUserRepository(UserRepository inner) {
        this.inner = inner;
        this.userCache = new CacheImpl<>("userRepoStringLong", String.class, Long.class, 5, 300);
    }

    @Override
    public User create(User user) {
        return inner.create(user);
    }

    @Override
    public Optional<User> get(String userId) {
        return inner.get(userId);
    }

    @Override
    public Optional<User> byEmail(String email) {
        return inner.byEmail(email);
    }

    @Override
    public Optional<User> byGithubLogin(String githubLogin) {
        return inner.byGithubLogin(githubLogin);
    }

    @Override
    public Optional<User> byLinkedinId(String linkedinId) {
        return inner.byLinkedinId(linkedinId);
    }

    @Override
    public User update(User user) {
        return inner.update(user);
    }

    @Override
    public void delete(String userId) {
        inner.delete(userId);
    }

    @Override
    public Collection<User> getAll(boolean test) {
        return inner.getAll(test);
    }

    @Override
    public Collection<User> query(UserQuery userQuery) {
        return inner.query(userQuery);
    }

    @Override
    public long getTotalUsers() {
        return userCache.computeIfAbsent("totalUsers", s -> inner.getTotalUsers());
    }

    @Override
    public long getActiveUsersInRange(Instant from, Instant to) {
        String key = "activeUsersInRange_" + from.truncatedTo(DAYS).getEpochSecond() + "_" + to.truncatedTo(DAYS).getEpochSecond();
        Long found = userCache.get(key);
        if (found == null) {
            long activeUsers = inner.getActiveUsersInRange(from, to);
            userCache.put(key, activeUsers);
            return activeUsers;
        } else {
            return found;
        }
    }
}
