package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.repository.SolutionQuery;
import com.sdehunt.repository.SolutionRepository;
import lombok.Data;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class JdbiSolutionRepository implements SolutionRepository {

    private final String table;

    private final Jdbi jdbi;

    private String invalidStatuses = "'invalid_files','invalid_solution','timeout,error'";

    public JdbiSolutionRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`solution`";
    }

    @Override
    public String save(Solution s) {

        String id = UUID.randomUUID().toString();

        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (`id`, `task`, `user`, `repo`, `commit`, `status`, `score`, `cause`, `test`, `created`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", table),
                        id, s.getTaskId(), s.getUserId(), s.getRepo(), s.getCommit(), s.getStatus().name().toLowerCase(), s.getScore(), s.getCause(), s.isTest(), Instant.now().getEpochSecond()
                )
        );

        return id;
    }

    @Override
    public void update(Solution s) {
        jdbi.withHandle(
                db -> db.execute(
                        format("UPDATE %s SET `task` = ?, `user` = ?, `repo` = ?, `commit` = ?, `score` = ?, `cause` = ?, `status` = ? WHERE `id` = ?", table),
                        s.getTaskId(), s.getUserId(), s.getRepo(), s.getCommit(), s.getScore(), s.getCause(), s.getStatus(), s.getId()
                )
        );
    }

    @Override
    public Optional<Solution> get(String id) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id = ?", table), id)
                        .map(new SolutionRowMapper()).findFirst()
        );
    }

    @Override
    public void delete(String id) {
        jdbi.withHandle(db -> db.execute(format("DELETE FROM %s WHERE id = ?", table), id));
    }

    @Override
    public List<Solution> query(SolutionQuery request) {
        String sql = format("SELECT * FROM %s", table);
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

        String finalSql = sql + " ORDER BY `created` DESC";
        return jdbi.withHandle(
                handle -> handle.select(finalSql, params.toArray()).map(new SolutionRowMapper()).list()
        );
    }

    @Override
    public boolean isPresentForUser(Solution solution) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s  WHERE `user`= ? AND `repo` = ? AND `commit` = ?", table),
                        solution.getUserId(), solution.getRepo(), solution.getCommit())
                        .map(new SolutionRowMapper()).findFirst().isPresent()
        );
    }

    @Override
    public long getTotalSolutions() {
        String statuses = SolutionStatus.successful().stream().map(s -> "'" + s.name().toLowerCase() + "'").collect(Collectors.joining(","));
        return jdbi.withHandle(
                db -> db.select(format("SELECT count(*) FROM %s WHERE status = 'accepted' AND test = false AND status IN(" + statuses + ")", table))
                        .mapTo(Long.class).first()
        );
    }

    @Override
    public Map<String, List<String>> getAllRepos() {
        return jdbi.withHandle(
                db -> db.select(format("SELECT repo, user FROM %s GROUP BY repo", table))
                        .map(new UserRepoRowMapper())
                        .stream()
                        .collect(Collectors.groupingBy(UserRepo::getUserId, Collectors.mapping(UserRepo::getRepo, Collectors.toList())))
        );
    }

    @Override
    public Map<String, List<String>> getTasksRepos(Set<String> taskIds) {
        String tasksStr = taskIds.stream()
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(","));
        return jdbi.withHandle(
                db -> db.select(format("SELECT repo, user FROM %s WHERE task IN (" + tasksStr + ") GROUP BY repo", table))
                        .map(new UserRepoRowMapper())
                        .stream()
                        .collect(Collectors.groupingBy(UserRepo::getUserId, Collectors.mapping(UserRepo::getRepo, Collectors.toList())))
        );
    }

    @Override
    public int getNumberUsersSolvedTask(String taskId, Set<SolutionStatus> statuses) {
        String statusString = statuses.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
        String statusQuery = statuses.isEmpty() ? "" : " AND status IN (" + statusString + ")";
        return jdbi.withHandle(
                db -> db.select(format("SELECT count(distinct user) FROM %s WHERE task = ? AND test = false" + statusQuery, table), taskId)
                        .mapTo(Integer.class).first()
        );
    }

    @Override
    public Set<String> solvedTasks(Set<String> taskIds) {
        if (taskIds.isEmpty()) {
            return Collections.emptySet();
        }
        String taskIdsStr = taskIds.stream().map(t -> "'" + t + "'").collect(Collectors.joining(","));
        return new HashSet<>(jdbi.withHandle(
                db -> db.select(format("SELECT distinct user FROM %s WHERE test = false AND task IN (" + taskIdsStr + ") AND status NOT IN (" + invalidStatuses + ")", table))
                        .mapTo(String.class).list()
        ));
    }

    private class SolutionRowMapper implements RowMapper<Solution> {
        @Override
        public Solution map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Solution()
                    .setId(rs.getString("id"))
                    .setTaskId(rs.getString("task"))
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

    private class UserRepoRowMapper implements RowMapper<UserRepo> {
        @Override
        public UserRepo map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new UserRepo()
                    .setUserId(rs.getString("user"))
                    .setRepo(rs.getString("repo"));
        }
    }

    @Data
    private class UserRepo {
        private String repo;
        private String userId;
    }
}
