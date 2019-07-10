package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.User;
import com.sdehunt.repository.UserQuery;
import com.sdehunt.repository.UserRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

import static java.lang.String.format;

public class JdbiUserRepository implements UserRepository {
    private final static String TABLE = "`sdehunt_db`.`user`";
    private Jdbi jdbi;

    public JdbiUserRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public User create(User u) {
        u.setId(UUID.randomUUID().toString());
        long now = Instant.now().getEpochSecond();
        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (`id`, `name`, `nickname`, `email`, `github_login`, `linkedin_id`, `image_url`, `test`, `created`, `updated`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE),
                        u.getId(), u.getName(), u.getNickname(), u.getEmail(), u.getGithubLogin(), u.getLinkedinId(), u.getImageUrl(), u.isTest(), now, now
                ));

        return get(u.getId()).orElse(null);
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
    public Optional<User> byGithubLogin(String githubLogin) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE github_login = ?", TABLE), githubLogin)
                        .map(new UserRowMapper()).findFirst()
        );
    }

    @Override
    public Optional<User> byLinkedinId(String linkedinId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE linkedin_id = ?", TABLE), linkedinId)
                        .map(new UserRowMapper()).findFirst()
        );
    }

    @Override
    public User update(User updateRequest) {
        User user = get(updateRequest.getId()).orElseThrow();
        setFields(user, updateRequest);

        jdbi.withHandle(
                db -> db.execute(
                        format("UPDATE %s SET name = ?, nickname = ?, email = ?, github_login = ?, linkedin_id = ?, image_url = ?, updated = ?, solved = ?, avg_rank = ?, last_submit = ? WHERE id = ?", TABLE),
                        user.getName(), user.getNickname(), user.getEmail(), user.getGithubLogin(), user.getLinkedinId(), user.getImageUrl(), Instant.now().getEpochSecond(), user.getSolved(), user.getAvgRank(), Optional.ofNullable(user.getLastSubmit()).map(Instant::getEpochSecond).orElse(null), user.getId())
        );

        return get(user.getId()).orElseThrow();
    }

    private void setFields(User existing, User u) {
        Optional.ofNullable(u.getName()).ifPresent(existing::setName);
        Optional.ofNullable(u.getNickname()).ifPresent(existing::setNickname);
        Optional.ofNullable(u.getImageUrl()).ifPresent(existing::setImageUrl);
        Optional.ofNullable(u.getEmail()).ifPresent(existing::setEmail);
        Optional.ofNullable(u.getGithubLogin()).ifPresent(existing::setGithubLogin);
        Optional.ofNullable(u.getLinkedinId()).ifPresent(existing::setLinkedinId);
        Optional.ofNullable(u.getUserName()).ifPresent(existing::setUserName);
        Optional.ofNullable(u.getLastSubmit()).ifPresent(existing::setLastSubmit);
        Optional.ofNullable(u.getSolved()).ifPresent(existing::setSolved);
        Optional.ofNullable(u.getAvgRank()).ifPresent(existing::setAvgRank);
    }

    @Override
    public void delete(String userId) {
        jdbi.withHandle(
                db -> db.execute(format("DELETE FROM %s WHERE id = ?", TABLE), userId)
        );
    }

    @Override
    public Collection<User> getAll(boolean test) {
        return jdbi.withHandle(
                db -> db.select(getAllQuery(test))
                        .map(new UserRowMapper()).list()
        );
    }

    @Override
    public Collection<User> query(UserQuery query) {
        String sql = format("SELECT * FROM %s", TABLE);
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        query.getNickname().ifPresent(nickname -> {
            conditions.add("`nickname` = ?");
            params.add(nickname);
        });
        query.getEmail().ifPresent(email -> {
            conditions.add("`email` = ?");
            params.add(email);
        });
        query.getGithubLogin().ifPresent(githubLogin -> {
            conditions.add("`github_login` = ?");
            params.add(githubLogin);
        });
        query.getLinkedinId().ifPresent(linkedinId -> {
            conditions.add("`linkedin_id` = ?");
            params.add(linkedinId);
        });

        if (!query.isTest()) {
            conditions.add("`test` = false");
        }

        if (!conditions.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", conditions);
        }

        String finalSql = sql;
        return jdbi.withHandle(
                handle -> handle.select(finalSql, params.toArray()).map(new UserRowMapper()).list()
        );
    }

    private String getAllQuery(boolean test) {
        final String query = format("SELECT * FROM %s", TABLE);
        final String testClause = " WHERE test = false";
        return test ? query : query + testClause;
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User map(ResultSet rs, StatementContext ctx) throws SQLException {
            String userName = rs.getString("nickname");
            if (userName == null || userName.isEmpty() || userName.isBlank()) {
                userName = rs.getString("github_login");
            }
            return new User()
                    .setId(rs.getString("id"))
                    .setName(rs.getString("name"))
                    .setNickname(rs.getString("nickname"))
                    .setEmail(rs.getString("email"))
                    .setGithubLogin(rs.getString("github_login"))
                    .setLinkedinId(rs.getString("linkedin_id"))
                    .setImageUrl(rs.getString("image_url"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")))
                    .setUpdated(Instant.ofEpochSecond(rs.getLong("updated")))
                    .setTest(rs.getBoolean("test"))
                    .setSolved(rs.getInt("solved"))
                    .setAvgRank(rs.getString("avg_rank") != null ? Integer.valueOf(rs.getString("avg_rank")) : null)
                    .setLastSubmit(Instant.ofEpochSecond(rs.getLong("last_submit")))
                    .setUserName(userName);
        }
    }
}
