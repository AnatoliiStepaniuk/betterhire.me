package com.sdehunt.repository.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Task;
import com.sdehunt.repository.TaskRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class JdbiTaskRepository implements TaskRepository {

    private final static String TABLE = "`sdehunt_db`.`task`";
    private Jdbi jdbi;

    public JdbiTaskRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public List<Task> getAll() {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s", TABLE)).map(new TaskRowMapper()).list()
        );
    }

    @Override
    public List<ShortTask> getAllShort() {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s", TABLE)).map(new ShortTaskRowMapper()).list() // TODO specific fields
        );
    }

    @Override
    public Optional<Task> get(String id) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id = ?", TABLE), id)
                        .map(new TaskRowMapper()).findFirst()
        );
    }

    @Override
    public Optional<ShortTask> getShort(String id) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id = ?", TABLE), id)
                        .map(new ShortTaskRowMapper()).findFirst()
        );
    }

    @Override
    public void delete(String id) {
        jdbi.withHandle(
                db -> db.execute(format("DELETE FROM %s WHERE id = ?", TABLE), id)
        );
    }

    @Override
    public void update(Task task) {
        long now = Instant.now().getEpochSecond();
        jdbi.withHandle(
                db -> db.execute(
                        format("UPDATE %s SET name = ?, short_description = ?, description = ?, updated = ? WHERE id = ?", TABLE),
                        task.getName(), task.getShortDescription(), task.getDescription(), now, task.getId()
                )
        );
    }

    private class TaskRowMapper implements RowMapper<Task> { // TODO tests
        @Override
        public Task map(ResultSet rs, StatementContext ctx) throws SQLException {
            Task task = new Task();
            task.setDescription(rs.getString("description"))
                    .setId(TaskID.of(rs.getString("id")))
                    .setName(rs.getString("name"))
                    .setShortDescription(rs.getString("short_description"))
                    .setImageUrl(rs.getString("image_url"))
                    .setSubmittable(rs.getBoolean("submittable"))
                    .setEnabled(rs.getBoolean("enabled"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")))
                    .setUpdated(Instant.ofEpochSecond(rs.getLong("updated")));
            return task;
        }
    }

    private class ShortTaskRowMapper implements RowMapper<ShortTask> { // TODO tests
        @Override
        public ShortTask map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new ShortTask()
                    .setId(TaskID.of(rs.getString("id")))
                    .setName(rs.getString("name"))
                    .setShortDescription(rs.getString("short_description"))
                    .setImageUrl(rs.getString("image_url"))
                    .setSubmittable(rs.getBoolean("submittable"))
                    .setEnabled(rs.getBoolean("enabled"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")))
                    .setUpdated(Instant.ofEpochSecond(rs.getLong("updated")));
        }
    }
}
