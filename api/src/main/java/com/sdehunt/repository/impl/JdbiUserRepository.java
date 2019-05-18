package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.User;
import com.sdehunt.commons.model.impl.UserImpl;
import com.sdehunt.repository.UserRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

public class JdbiUserRepository implements UserRepository {
    private final static String TABLE = "`sdehunt_db`.`user`";
    private Jdbi jdbi;

    public JdbiUserRepository(Connection connection) {
        this.jdbi = Jdbi.create(connection);
    }

    @Override
    public User create(User user) {
        user.setId(UUID.randomUUID().toString());
        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (`id`, `email`, `github`, `linkedin`, `created`) VALUES(?, ?, ?, ?, ?)", TABLE),
                        user.getId(), user.getEmail(), user.getGithub(), user.getLinkedIn(), Instant.now().getEpochSecond()
                ));

        return get(user.getId()).orElse(null);
    }

    @Override
    public Optional<User> get(String userId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id = ?", TABLE), userId)
        ).map(new UserRowMapper()).findFirst();
    }

    @Override
    public User update(User user) {
        jdbi.withHandle(
                db -> db.execute(
                        format("UPDATE %s SET email = ?, github = ?, linkedin = ? WHERE id = ?", TABLE),
                        user.getEmail(), user.getGithub(), user.getLinkedIn(), user.getId())
        );

        return get(user.getId()).orElse(null); // TODO throw exception if not found
    }

    @Override
    public void delete(String userId) {
        jdbi.withHandle(
                db -> db.execute(format("DELETE FROM %s WHERE id = ?", TABLE), userId)
        );
    }

    @Override
    public Collection<User> getAll() {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s", TABLE))
        ).map(new UserRowMapper()).list();
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new UserImpl(
                    rs.getString("id"),
                    rs.getString("email"),
                    rs.getString("github"),
                    rs.getString("linkedin"),
                    Instant.ofEpochSecond(rs.getLong("created"))
            );
        }
    }
}
