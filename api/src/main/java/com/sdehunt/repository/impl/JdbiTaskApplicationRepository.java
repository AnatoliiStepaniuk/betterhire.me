package com.sdehunt.repository.impl;

import com.sdehunt.repository.TaskApplicationRepository;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;
import java.time.Instant;

import static java.lang.String.format;

public class JdbiTaskApplicationRepository implements TaskApplicationRepository {

    private final String table;

    private final Jdbi jdbi;

    public JdbiTaskApplicationRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`task_application`";
    }

    @Override
    public void save(String company, String contact, String task, String jobUrl, String taskUrl) {
        long now = Instant.now().getEpochSecond();
        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (`company`, `contact`, `task`, `job_url`, `task_url`, `created`) VALUES(?, ?, ?, ?, ?, ?)", table),
                        company, contact, task, jobUrl, taskUrl, now
                ));
    }
}
