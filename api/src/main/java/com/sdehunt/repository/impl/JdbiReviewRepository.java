package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.Review;
import com.sdehunt.repository.ReviewRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class JdbiReviewRepository implements ReviewRepository {

    private final String table;

    private final Jdbi jdbi;

    public JdbiReviewRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`solution_review`";
    }

    @Override
    public void create(String solutionId, String userId, String taskId, Long grade, String comment, String emoji, String reviewer) {
        String id = UUID.randomUUID().toString();
        jdbi.withHandle(
                db -> db.execute(format("INSERT INTO %s (`id`, `user`, `task`, `solution`, `grade`, `comment`, `emoji`, `reviewer`, `created`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", table),
                        id, userId, taskId, solutionId, grade, comment, emoji, reviewer, Instant.now().getEpochSecond())
        );
    }

    @Override
    public List<Review> forUser(String userId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE user = ? ORDER BY created DESC", table), userId)
                        .map(new ReviewRowMapper()).list()
        );
    }

    static <T> Collector<T, ?, List<T>> toSortedList(Comparator<? super T> c) {
        return Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(c)), ArrayList::new);
    }

    @Override
    public Map<String, List<Review>> forUsers(Set<String> userIds) {
        if (userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String userIdsString = userIds.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
        return sort(jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE user IN (%s)", table, userIdsString))
                        .map(new ReviewRowMapper())
                        .stream()
                        .collect(Collectors.groupingBy(Review::getUserId))
        ));
    }

    private Map<String, List<Review>> sort(Map<String, List<Review>> in) {
        return in.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    Collections.sort(e.getValue());
                    return e.getValue();
                }));
    }

    @Override
    public List<Review> forUserAndTask(String userId, String taskId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? AND user = ? ORDER BY created DESC", table), taskId, userId)
                        .map(new ReviewRowMapper()).list()
        );
    }

    private class ReviewRowMapper implements RowMapper<Review> {
        @Override
        public Review map(ResultSet rs, StatementContext ctx) throws SQLException {

            return new Review()
                    .setId(rs.getString("id"))
                    .setTaskId(rs.getString("task"))
                    .setUserId(rs.getString("user"))
                    .setSolutionId(rs.getString("solution"))
                    .setGrade(Optional.ofNullable(rs.getString("grade")).map(Long::valueOf).orElse(null))
                    .setComment(Optional.ofNullable(rs.getString("comment")).orElse(null))
                    .setEmoji(Optional.ofNullable(rs.getString("emoji")).orElse(null))
                    .setReviewer(rs.getString("reviewer"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")));
        }
    }
}
