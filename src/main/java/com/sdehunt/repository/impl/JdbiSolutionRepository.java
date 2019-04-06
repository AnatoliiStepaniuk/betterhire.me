package com.sdehunt.repository.impl;

import com.sdehunt.model.Solution;
import com.sdehunt.model.impl.SolutionImpl;
import com.sdehunt.repository.SolutionQuery;
import com.sdehunt.repository.SolutionRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

public class JdbiSolutionRepository implements SolutionRepository {

    private static final String TABLE = "`sdehunt_db`.`solution`";

    private Jdbi jdbi;

    public JdbiSolutionRepository(Connection connection) {
        this.jdbi = Jdbi.create(connection);
    }

    @Override
    public String save(Solution s) {

        String id = UUID.randomUUID().toString();

        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (`id`, `task_id`, `user_id`, `repo_id`, `commit`, `score`, `created`) values (?, ?, ?, ?, ?)", TABLE),
                        id, s.getTaskId(), s.getUserId(), s.getRepoId(), s.getCommit(), s.getScore(), Instant.now().getEpochSecond()
                )
        );

        return id;
    }

    @Override
    public Optional<Solution> get(String id) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id = ?", TABLE), id)
        ).map(new SolutionRowMapper()).findFirst();
    }

    @Override
    public void delete(String id) {
        jdbi.withHandle(db -> db.execute(format("DELETE FROM %s WHERE id = ?", TABLE), id));
    }

    @Override
    public List<Solution> query(SolutionQuery request) {
        String sql = format("SELECT * FROM %s", TABLE);
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        request.getTask().ifPresent(taskId -> {
            conditions.add("`task_id` = ?");
            params.add(taskId);
        });
        request.getUser().ifPresent(userId -> {
            conditions.add("`user_id` = ?");
            params.add(userId);
        });

        if (!conditions.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", conditions);
        }

        String finalSql = sql;
        return jdbi.withHandle(
                handle -> handle.select(finalSql, params.toArray())
        ).map(new SolutionRowMapper()).list();
    }

    private class SolutionRowMapper implements RowMapper<Solution> {
        @Override
        public Solution map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new SolutionImpl(
                    rs.getString("id"),
                    rs.getString("task_id"),
                    rs.getString("user_id"),
                    rs.getString("repo_id"),
                    rs.getString("commit"),
                    Long.valueOf(rs.getString("score")),
                    Instant.ofEpochSecond(rs.getLong("created"))
            );
        }
    }
}
