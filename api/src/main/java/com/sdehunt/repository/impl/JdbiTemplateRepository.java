package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.Template;
import com.sdehunt.repository.TemplateRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.lang.String.format;

public class JdbiTemplateRepository implements TemplateRepository {

    private final String table;

    private final Jdbi jdbi;

    public JdbiTemplateRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`template`";
    }

    @Override
    public Optional<Template> find(String taskId, Language language) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? AND language = ?", table), taskId, language)
                        .map(new TemplateRowMapper()).findOne()
        );
    }

    private class TemplateRowMapper implements RowMapper<Template> {
        @Override
        public Template map(ResultSet rs, StatementContext ctx) throws SQLException {

            return new Template()
                    .setTaskId(rs.getString("task"))
                    .setRepo(rs.getString("repo"));
        }
    }
}
