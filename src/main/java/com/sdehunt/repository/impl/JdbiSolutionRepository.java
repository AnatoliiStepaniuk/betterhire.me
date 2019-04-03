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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class JdbiSolutionRepository implements SolutionRepository {

    private Jdbi jdbi;

    public JdbiSolutionRepository(Connection connection) {
        this.jdbi = Jdbi.create(connection);
    }


    @Override
    public String save(Solution solution) {

        String id = UUID.randomUUID().toString();

        jdbi.withHandle(
                handle -> handle.execute(
                        "INSERT INTO `sdehunt_db`.`solution` (`id`, `user_id`, `task_id`, `score`, `created`) values (?, ?, ?, ?, ?)",
                        id, solution.getUserId(), solution.getTaskId(), solution.getScore(), Instant.now().getEpochSecond()
                )
        );

        return id;
    }

    @Override
    public List<Solution> query(SolutionQuery request) {
        String sql = "SELECT * FROM `sdehunt_db`.`solution`";
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
        ).map(new SolutionRowMapper())
                .list();
    }

    private class SolutionRowMapper implements RowMapper<Solution> {
        @Override
        public Solution map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new SolutionImpl(
                    rs.getString("id"),
                    rs.getString("task_id"),
                    rs.getString("user_id"),
                    Collections.EMPTY_LIST,
                    Long.valueOf(rs.getString("score")),
                    Instant.ofEpochSecond(rs.getLong("created"))
            );
        }
    }
}
