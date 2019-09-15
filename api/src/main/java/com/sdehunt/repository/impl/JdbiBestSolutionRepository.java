package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.BestSolution;
import com.sdehunt.commons.model.BestTaskResult;
import com.sdehunt.commons.model.BestUserResult;
import com.sdehunt.repository.BestSolutionRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

public class JdbiBestSolutionRepository implements BestSolutionRepository {

    private final String table;
    private final String userTable;

    private final Jdbi jdbi;

    public JdbiBestSolutionRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`best_solution`";
        this.userTable = "`" + db + "`.`user`";
    }

    @Override
    public List<BestSolution> getForTask(String taskId, boolean test) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? AND test = ? ORDER BY score DESC", table), taskId, test)
                        .map(new BestSolutionRowMapper()).list()
        );
    }

    @Override
    public List<BestSolution> getForUser(String userId, boolean test) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE user = ? AND test = ?", table), userId, test)
                        .map(new BestSolutionRowMapper()).list()
        );
    }

    @Override
    public void save(Collection<BestSolution> bestSolutions) {
        if (bestSolutions.isEmpty()) {
            return;
        }
        StringBuilder sql = new StringBuilder(format("INSERT INTO %s (`user`, `task`, `solution`, `rank`, `score`, `test`) VALUES ", table));
        List<String> args = new ArrayList<>();
        List<BestSolution> bestSolutionsList = new ArrayList<>(bestSolutions);
        for (int i = 0; i < bestSolutionsList.size(); i++) {
            BestSolution bs = bestSolutionsList.get(i);
            args.add(bs.getUserId());
            args.add(bs.getTaskId());
            args.add(bs.getSolutionId());
            args.add(String.valueOf(bs.getRank()));
            args.add(String.valueOf(bs.getScore()));
            args.add(bs.isTest() ? "1" : "0");
            sql.append("(?, ?, ?, ?, ?, ?)");
            if (i != bestSolutionsList.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(" ON DUPLICATE KEY UPDATE user=VALUES(user), " +
                "task=VALUES(task), " +
                "solution=VALUES(solution), " +
                "rank=VALUES(rank), " +
                "score=VALUES(score), " +
                "test=VALUES(test)");

        jdbi.withHandle(db -> db.execute(sql.toString(), args.toArray()));
    }

    @Override
    public List<BestTaskResult> bestTaskResults(String taskId, boolean test) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT score, github_login, nickname FROM %s bs INNER JOIN %s u ON bs.`user` = u.`id`" +
                        " WHERE `task` = ? AND bs.`test` = ? AND u.`test` = ? ORDER BY score DESC", table, userTable), taskId, test, test)
                        .map(new BestTaskResultRowMapper()).list()
        );
    }

    @Override
    public List<BestUserResult> bestUserResults(String userId, boolean test) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE `user` = ? AND `test` = ?", table), userId, test)
                        .map(new BestUserResultRowMapper()).list()
        );
    }

    private class BestSolutionRowMapper implements RowMapper<BestSolution> {
        @Override
        public BestSolution map(ResultSet rs, StatementContext ctx) throws SQLException {

            return new BestSolution()
                    .setUserId(rs.getString("user"))
                    .setTaskId(rs.getString("task"))
                    .setScore(rs.getLong("score"))
                    .setSolutionId(rs.getString("solution"))
                    .setRank(rs.getInt("rank"))
                    .setTest(rs.getBoolean("test"));
        }
    }

    private class BestTaskResultRowMapper implements RowMapper<BestTaskResult> {
        @Override
        public BestTaskResult map(ResultSet rs, StatementContext ctx) throws SQLException {
            String userName = rs.getString("nickname");
            if (userName == null || userName.isEmpty() || userName.isBlank()) {
                userName = rs.getString("github_login");
            }
            return new BestTaskResult()
                    .setScore(rs.getLong("score"))
                    .setUserName(userName);
        }
    }

    private class BestUserResultRowMapper implements RowMapper<BestUserResult> {
        @Override
        public BestUserResult map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new BestUserResult()
                    .setTaskId(rs.getString("task"))
                    .setRank(rs.getLong("rank"));
        }
    }
}
