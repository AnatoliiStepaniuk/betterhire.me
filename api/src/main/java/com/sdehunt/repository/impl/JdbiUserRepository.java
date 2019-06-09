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
                        format("INSERT INTO %s (`id`, `name`, `nickname`, `email`, `github_login`, `linkedin_id`, `created`, `updated`) VALUES(?, ?, ?, ?, ?, ?, ?, ?)", TABLE),
                        user.getId(), user.getName(), user.getNickname(), user.getEmail(), user.getGithubLogin(), user.getLinkedinId(), now, now
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
    public Optional<User> byEmail(String email) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE email = ?", TABLE), email) // TODO make unique column?
                        .map(new UserRowMapper()).findFirst()
        );
    }

    @Override
    public Optional<User> byGithubLogin(String github) {
        return Optional.empty(); // TODO
    }

    @Override
    public Optional<User> byLinkedinId(String linkedin) {
        return Optional.empty(); // TODO
    }

    @Override
    public User update(User user) {
        jdbi.withHandle(
                db -> db.execute(
                        format("UPDATE %s SET name = ?, nickname = ?, email = ?, github_login = ?, linkedin_id = ?, updated = ? WHERE id = ?", TABLE),
                        user.getName(), user.getNickname(), user.getEmail(), user.getGithubLogin(), user.getLinkedinId(), Instant.now().getEpochSecond(), user.getId())
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
                    .setName(rs.getString("name"))
                    .setNickname(rs.getString("nickname"))
                    .setEmail(rs.getString("email"))
                    .setGithubLogin(rs.getString("github_login"))
                    .setLinkedinId(rs.getString("linkedin_id"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")))
                    .setUpdated(Instant.ofEpochSecond(rs.getLong("updated"))
                    );
        }
    }
}
