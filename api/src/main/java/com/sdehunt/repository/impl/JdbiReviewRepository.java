package com.sdehunt.repository.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Review;
import com.sdehunt.repository.ReviewRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

public class JdbiReviewRepository implements ReviewRepository {

    private final String table;

    private final Jdbi jdbi;

    public JdbiReviewRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`solution_review`";
    }

    @Override
    public void create(String solutionId, String userId, TaskID taskID, Long grade, String comment, String emoji, String reviewer) {
        String id = UUID.randomUUID().toString();
        jdbi.withHandle(
                db -> db.execute(format("INSERT INTO %s (`id`, `user`, `task`, `solution`, `grade`, `comment`, `emoji`, `reviewer`, `created`) VALUES (?, ?, ?, ?, ?, ?, ?,  ?, ?)", table),
                        id, userId, taskID, solutionId, grade, comment, emoji, reviewer, Instant.now().getEpochSecond())
        );
    }

    @Override
    public List<Review> forUser(String userId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE user = ? ORDER BY created DESC", table), userId)
                        .map(new ReviewRowMapper()).list()
        );
    }

    @Override
    public List<Review> forUserAndTask(String userId, TaskID taskID) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? AND user = ? ORDER BY created DESC", table), taskID, userId)
                        .map(new ReviewRowMapper()).list()
        );
    }

    private class ReviewRowMapper implements RowMapper<Review> {
        @Override
        public Review map(ResultSet rs, StatementContext ctx) throws SQLException {

            return new Review()
                    .setId(rs.getString("id"))
                    .setTaskID(TaskID.of(rs.getString("task")))
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