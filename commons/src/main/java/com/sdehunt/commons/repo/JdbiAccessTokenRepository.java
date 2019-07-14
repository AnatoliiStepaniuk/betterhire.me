package com.sdehunt.commons.repo;

import com.sdehunt.commons.security.AccessToken;
import com.sdehunt.commons.security.OAuthProvider;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.lang.String.format;

public class JdbiAccessTokenRepository implements AccessTokenRepository {

    private final String table;

    private Jdbi jdbi;

    public JdbiAccessTokenRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`user_access_token`";
    }

    @Override
    public void save(String userId, OAuthProvider provider, String token) {
        jdbi.withHandle(
                db -> db.execute(format("INSERT INTO %s (`user`, `provider`, `token`) values (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE `token` = ?", table), userId, provider.name().toLowerCase(), token, token)
        );
    }

    @Override
    public Optional<AccessToken> find(String userId, OAuthProvider provider) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE user = ? AND provider = ?", table), userId, provider.name().toLowerCase())
                        .map(new AccessTokenRowMapper()).findFirst()
        );
    }

    private class AccessTokenRowMapper implements RowMapper<AccessToken> {
        @Override
        public AccessToken map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new AccessToken()
                    .setToken(rs.getString("token"))
                    .setProvider(OAuthProvider.valueOf(rs.getString("provider").toUpperCase()));
        }
    }

}
