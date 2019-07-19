package com.sdehunt.repository.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.SolutionRepo;
import com.sdehunt.repository.SolutionRepoRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

import static java.lang.String.format;

public class JdbiSolutionRepo implements SolutionRepoRepository {

    private final String table;

    private final Jdbi jdbi;

    public JdbiSolutionRepo(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`solution_repo`";
    }

    @Override
    public Optional<SolutionRepo> find(TaskID taskId, String userId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? AND user = ? ORDER BY id DESC", table), taskId, userId)
                        .map(new SolutionRepoRowMapper()).findFirst()
        );
    }

    @Override
    public void save(TaskID taskId, String userId, String repo, String webhookSecret) {
        jdbi.withHandle(
                db -> db.execute(format("INSERT INTO %s (`task`, `user`, `repo`, `webhook_secret`, `created`) VALUES (?, ?, ?, ?, ?)", table),
                        taskId, userId, repo, webhookSecret, Instant.now().getEpochSecond())
        );
    }

    private class SolutionRepoRowMapper implements RowMapper<SolutionRepo> {
        @Override
        public SolutionRepo map(ResultSet rs, StatementContext ctx) throws SQLException {

            return new SolutionRepo()
                    .setId(rs.getLong("id"))
                    .setTaskID(TaskID.of(rs.getString("task")))
                    .setUserId(rs.getString("user"))
                    .setRepo(rs.getString("repo"))
                    .setWebhookSecret(Optional.ofNullable(rs.getString("webhook_secret")).orElse(null))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")));
        }
    }
}
