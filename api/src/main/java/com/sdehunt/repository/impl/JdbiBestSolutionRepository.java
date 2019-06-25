package com.sdehunt.repository.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.BestSolution;
import com.sdehunt.repository.BestSolutionRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class JdbiBestSolutionRepository implements BestSolutionRepository {

    private static final String TABLE = "`sdehunt_db`.`best_solution`";

    private Jdbi jdbi;

    public JdbiBestSolutionRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public List<BestSolution> getForTask(TaskID taskID, boolean test) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? AND test = ? ORDER BY score DESC", TABLE), taskID, test)
                        .map(new BestSolutionRowMapper()).list()
        );
    }

    @Override
    public List<BestSolution> getForUser(String userId, boolean test) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE user = ? AND test = ?", TABLE), userId, test)
                        .map(new BestSolutionRowMapper()).list()
        );
    }

    @Override
    public void save(List<BestSolution> bestSolutions) {
        if (bestSolutions.isEmpty()) {
            return;
        }
        StringBuilder sql = new StringBuilder(format("INSERT INTO %s (`user`, `task`, `rank`, `score`, `test`) VALUES ", TABLE));
        List<String> args = new ArrayList<>();
        for (int i = 0; i < bestSolutions.size(); i++) {
            BestSolution bs = bestSolutions.get(i);
            args.add(bs.getUserId());
            args.add(bs.getTaskID().name().toLowerCase());
            args.add(String.valueOf(bs.getRank()));
            args.add(String.valueOf(bs.getScore()));
            args.add(bs.isTest() ? "1" : "0");
            sql.append("(?, ?, ?, ?, ?)");
            if (i != bestSolutions.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(" ON DUPLICATE KEY UPDATE user=VALUES(user), " +
                "task=VALUES(task), " +
                "rank=VALUES(rank), " +
                "score=VALUES(score), " +
                "test=VALUES(test)");

        jdbi.withHandle(db -> db.execute(sql.toString(), args.toArray()));
    }

    private class BestSolutionRowMapper implements RowMapper<BestSolution> {
        @Override
        public BestSolution map(ResultSet rs, StatementContext ctx) throws SQLException {

            return new BestSolution()
                    .setScore(rs.getLong("score"))
                    .setUserId(rs.getString("user"))
                    .setRank(rs.getInt("rank"))
                    .setTaskID(TaskID.of(rs.getString("task")))
                    .setTest(rs.getBoolean("test"));
        }
    }

}
