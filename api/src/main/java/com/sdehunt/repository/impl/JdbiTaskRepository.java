package com.sdehunt.repository.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.impl.TaskImpl;
import com.sdehunt.repository.TaskRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class JdbiTaskRepository implements TaskRepository {

    private final static String TABLE = "`sdehunt_db`.`task`";
    private Jdbi jdbi;

    public JdbiTaskRepository(Connection connection) {
        this.jdbi = Jdbi.create(connection);
    }

    @Override
    public List<Task> getAll() {
        return jdbi.withHandle(db -> db.select(format("SELECT * FROM %s", TABLE)))
                .map(new TaskRowMapper())
                .list();
    }

    @Override
    public Optional<Task> get(String id) {
        return jdbi.withHandle(db -> db.select(format("SELECT * FROM %s WHERE id = ?", TABLE), id))
                .map(new TaskRowMapper())
                .findFirst();
    }

    @Override
    public void delete(String id) {
        jdbi.withHandle(
                db -> db.execute(format("DELETE FROM %s WHERE id = ?", TABLE), id)
        );
    }

    @Override
    public void create(Task task) {
        long created = Instant.now().getEpochSecond();
        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (id, description, created) VALUES (?, ?, ?)", TABLE),
                        task.getId(), task.getDescription(), created
                )
        );
    }

    private class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new TaskImpl(
                    TaskID.valueOf(rs.getString("id").toUpperCase()),
                    rs.getString("description"),
                    Instant.ofEpochSecond(rs.getLong("created"))
            );
        }
    }
}
