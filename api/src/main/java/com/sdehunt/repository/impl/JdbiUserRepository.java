package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.User;
import com.sdehunt.repository.UserRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
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

    public JdbiUserRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public User create(User user) {
        user.setId(UUID.randomUUID().toString());
        long now = Instant.now().getEpochSecond();
        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (`id`, `email`, `github`, `linkedin`, `created`, `updated`) VALUES(?, ?, ?, ?, ?, ?)", TABLE),
                        user.getId(), user.getEmail(), user.getGithub(), user.getLinkedIn(), now, now
                ));

        return get(user.getId()).orElse(null);
    }

    @Override
    public Optional<User> get(String userId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id = ?", TABLE), userId)
                        .map(new UserRowMapper()).findFirst()
        );
    }

    @Override
    public User update(User user) {
        jdbi.withHandle(
                db -> db.execute(
                        format("UPDATE %s SET email = ?, github = ?, linkedin = ?, updated = ? WHERE id = ?", TABLE),
                        user.getEmail(), user.getGithub(), user.getLinkedIn(), Instant.now().getEpochSecond(), user.getId())
        );

        return get(user.getId()).orElse(null); // TODO  throw exception if not found
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
                        .map(new UserRowMapper()).list()
        );
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new User()
                    .setId(rs.getString("id"))
                    .setEmail(rs.getString("email"))
                    .setGithub(rs.getString("github"))
                    .setLinkedIn(rs.getString("linkedin"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")))
                    .setUpdated(Instant.ofEpochSecond(rs.getLong("updated"))
                    );
        }
    }
}
