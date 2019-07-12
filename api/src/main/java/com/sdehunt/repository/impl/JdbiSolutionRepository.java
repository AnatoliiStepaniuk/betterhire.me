package com.sdehunt.repository.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.repository.SolutionQuery;
import com.sdehunt.repository.SolutionRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
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

    public JdbiSolutionRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public String save(Solution s) {

        String id = UUID.randomUUID().toString();

        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (`id`, `task`, `user`, `repo`, `commit`, `score`, `cause`, `test`, `created`) values (?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE),
                        id, s.getTaskId(), s.getUserId(), s.getRepo(), s.getCommit(), s.getScore(), s.getCause(), s.isTest(), Instant.now().getEpochSecond()
                )
        );

        return id;
    }

    @Override
    public void update(Solution s) {
        jdbi.withHandle(
                db -> db.execute(
                        format("UPDATE %s SET `task` = ?, `user` = ?, `repo` = ?, `commit` = ?, `score` = ?, `cause` = ?, `status` = ? WHERE `id` = ?", TABLE),
                        s.getTaskId(), s.getUserId(), s.getRepo(), s.getCommit(), s.getScore(), s.getCause(), s.getStatus(), s.getId()
                )
        );
    }

    @Override
    public Optional<Solution> get(String id) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id = ?", TABLE), id)
                        .map(new SolutionRowMapper()).findFirst()
        );
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
            conditions.add("`task` = ?");
            params.add(taskId);
        });
        request.getUser().ifPresent(userId -> {
            conditions.add("`user` = ?");
            params.add(userId);
        });
        request.getStatus().ifPresent(status -> {
            conditions.add("`status` = ?");
            params.add(status);
        });

        if (!request.isTest()) {
            conditions.add("`test` = false");
        }

        if (!conditions.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", conditions);
        }

        String finalSql = sql;
        return jdbi.withHandle(
                handle -> handle.select(finalSql, params.toArray()).map(new SolutionRowMapper()).list()
        );
    }

    @Override
    public boolean isPresentForUser(Solution solution) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s  WHERE `user`= ? AND `repo` = ? AND `commit` = ?", TABLE),
                        solution.getUserId(), solution.getRepo(), solution.getCommit())
                        .map(new SolutionRowMapper()).findFirst().isPresent()
        );
    }

    private class SolutionRowMapper implements RowMapper<Solution> {
        @Override
        public Solution map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Solution()
                    .setId(rs.getString("id"))
                    .setTaskId(TaskID.of(rs.getString("task")))
                    .setUserId(rs.getString("user"))
                    .setRepo(rs.getString("repo"))
                    .setCommit(rs.getString("commit"))
                    .setScore(Long.valueOf(rs.getString("score")))
                    .setCause(rs.getString("cause"))
                    .setStatus(SolutionStatus.valueOf(rs.getString("status").toUpperCase()))
                    .setTest(rs.getBoolean("test"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")));
        }
    }
}
