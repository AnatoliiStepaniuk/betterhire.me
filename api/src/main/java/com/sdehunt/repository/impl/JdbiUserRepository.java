package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.commons.model.User;
import com.sdehunt.commons.util.EnumUtils;
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
import java.util.stream.Collectors;

import static java.lang.String.format;

public class JdbiUserRepository implements UserRepository {
    private final String userTable;
    private final String solutionTable;
    private final Jdbi jdbi;

    public JdbiUserRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.userTable = "`" + db + "`.`user`";
        this.solutionTable = "`" + db + "`.`solution`";
    }

    @Override
    public User create(User u) {
        u.setId(UUID.randomUUID().toString());
        long now = Instant.now().getEpochSecond();
        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (`id`, `name`, `nickname`, `email`, `cv`, `city`, `languages`, `phone`, `github_login`, `linkedin_id`, `company`, `image_url`, `test`, `created`, `updated`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", userTable),
                        u.getId(), u.getName(), u.getNickname(), u.getEmail(), u.getCv(), u.getCity(), EnumUtils.stringify(u.getLanguages()), u.getPhone(), u.getGithubLogin(), u.getLinkedinId(), u.getCompany(), u.getImageUrl(), u.isTest(), now, now
                ));

        return get(u.getId()).orElse(null);
    }

    private Set<Language> langsFromString(String langs) { // TODO duplicate
        return Optional.ofNullable(langs)
                .filter(t -> !t.isEmpty() && !t.isBlank())
                .map(String::toUpperCase)
                .map(s -> s.split(","))
                .map(t -> Arrays.stream(t).map(Language::valueOf).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    @Override
    public Optional<User> get(String userId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id = ?", userTable), userId)
                        .map(new UserRowMapper()).findFirst()
        );
    }

    @Override
    public Collection<User> getByIds(Collection<String> userIds) {
        if (userIds.isEmpty()) {
            return Collections.emptySet();
        }
        String userIdsStr = userIds.stream().map(id -> "\"" + id + "\"").collect(Collectors.joining(","));
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE id IN (" + userIdsStr + ")", userTable))
                        .map(new UserRowMapper()).list()
        );
    }

    @Override
    public Optional<User> byEmail(String email) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE email = ?", userTable), email) // TODO make unique column?
                        .map(new UserRowMapper()).findFirst()
        );
    }

    @Override
    public Optional<User> byGithubLogin(String githubLogin) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE github_login = ?", userTable), githubLogin)
                        .map(new UserRowMapper()).findFirst()
        );
    }

    @Override
    public Optional<User> byLinkedinId(String linkedinId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE linkedin_id = ?", userTable), linkedinId)
                        .map(new UserRowMapper()).findFirst()
        );
    }

    @Override
    public User update(User updateRequest) {
        User u = get(updateRequest.getId()).orElseThrow();
        setFields(u, updateRequest);

        jdbi.withHandle(
                db -> db.execute(
                        format("UPDATE %s SET name = ?, nickname = ?, email = ?, cv = ?, city = ?, languages = ?, phone = ?, github_login = ?, linkedin_id = ?, company = ?, image_url = ?, updated = ?, solved = ?, avg_rank = ?, last_submit = ?, available = ?, manager = ? WHERE id = ?", userTable),
                        u.getName(), u.getNickname(), u.getEmail(), u.getCv(), u.getCity(), EnumUtils.stringify(u.getLanguages()), u.getPhone(), u.getGithubLogin(), u.getLinkedinId(), u.getCompany(), u.getImageUrl(), Instant.now().getEpochSecond(), u.getSolved(), u.getAvgRank(), Optional.ofNullable(u.getLastSubmit()).map(Instant::getEpochSecond).orElse(null), u.getAvailable(), u.getManager(), u.getId())
        );

        return get(u.getId()).orElseThrow();
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
        Optional.ofNullable(u.getCv()).ifPresent(existing::setCv);
        Optional.ofNullable(u.getPhone()).ifPresent(existing::setPhone);
        Optional.ofNullable(u.getCity()).ifPresent(existing::setCity);
        Optional.ofNullable(u.getLanguages()).ifPresent(existing::setLanguages);
        Optional.ofNullable(u.getAvailable()).ifPresent(existing::setAvailable);
        Optional.ofNullable(u.getManager()).ifPresent(existing::setManager);
        Optional.ofNullable(u.getCompany()).ifPresent(existing::setCompany);
    }

    @Override
    public void delete(String userId) {
        jdbi.withHandle(
                db -> db.execute(format("DELETE FROM %s WHERE id = ?", userTable), userId)
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
    public Collection<User> query(UserQuery query) { // TODO add phone
        String sql = format("SELECT * FROM %s", userTable);
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

    @Override
    public Collection<User> getBySql(String sqlClause) {
        String sql = format("SELECT * FROM %s", userTable) + " WHERE " + sqlClause;
        return jdbi.withHandle(
                handle -> handle.select(sql).map(new UserRowMapper()).list()
        );

    }

    @Override
    public long getTotalUsers() {
        String statuses = SolutionStatus.successful().stream().map(s -> "'" + s.name().toLowerCase() + "'").collect(Collectors.joining(","));
        return jdbi.withHandle(db -> db.select(format("SELECT count(distinct user) FROM %s WHERE test = false AND status IN (" + statuses + ")", solutionTable))
                .mapTo(Long.class).first());
    }

    @Override
    public long getActiveUsersInRange(Instant from, Instant to) {
        return jdbi.withHandle(db -> db.select(format(
                "SELECT count(distinct user) FROM %s WHERE created >= %d AND created <= %d AND test = false",
                solutionTable, from.getEpochSecond(), to.getEpochSecond()
        )).mapTo(Long.class).first());
    }

    private String getAllQuery(boolean test) {
        String query = format("SELECT * FROM %s", userTable);
        final String testClause = " WHERE test = false";
        if (!test) {
            query += testClause;
        }
        return query + " ORDER BY avg_rank ASC";
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
                    .setCv(rs.getString("cv"))
                    .setCity(rs.getString("city"))
                    .setLanguages(langsFromString(rs.getString("languages")))
                    .setPhone(rs.getString("phone"))
                    .setGithubLogin(rs.getString("github_login"))
                    .setLinkedinId(rs.getString("linkedin_id"))
                    .setImageUrl(rs.getString("image_url"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")))
                    .setUpdated(Instant.ofEpochSecond(rs.getLong("updated")))
                    .setTest(rs.getBoolean("test"))
                    .setSolved(rs.getInt("solved"))
                    .setAvgRank(rs.getString("avg_rank") != null ? Integer.valueOf(rs.getString("avg_rank")) : null)
                    .setLastSubmit(Instant.ofEpochSecond(rs.getLong("last_submit")))
                    .setUserName(userName)
                    .setAvailable(rs.getBoolean("available"))
                    .setManager(rs.getBoolean("manager"))
                    .setCompany(rs.getString("company"));
        }
    }
}
