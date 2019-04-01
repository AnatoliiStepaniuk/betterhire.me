package com.sdehunt.repository;

import com.sdehunt.model.Task;
import com.sdehunt.model.impl.TaskImpl;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbiTaskRepository implements TaskRepository {

    private Jdbi jdbi;

    public JdbiTaskRepository(Connection connection) {
        this.jdbi = Jdbi.create(connection);
    }

    @Override
    public List<Task> getAll() {
        return jdbi.withHandle(handle -> handle.select("SELECT * FROM `sdehunt_db`.`task`"))
                .map(new TaskRowMapper())
                .list();
    }

    @Override
    public Optional<Task> get(String id) {
        return jdbi.withHandle(handle -> handle.select("SELECT * FROM `sdehunt_db`.`task` WHERE id = ?", id))
                .map(new TaskRowMapper())
                .findFirst();
    }

    private class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new TaskImpl(
                    rs.getString("id"),
                    rs.getString("description")
            );
        }
    }
}
