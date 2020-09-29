package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.TaskType;
import com.sdehunt.commons.util.EnumUtils;
import com.sdehunt.repository.TaskRepository;
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

public class JdbiTaskRepository implements TaskRepository {

    private final String table;
    private final Jdbi jdbi;

    public JdbiTaskRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`task`";
    }

    @Override
    public List<Task> getAll(boolean test) {
        return jdbi.withHandle(
                db -> db.select(getAllQuery(test)).map(new TaskRowMapper()).list()
        );
    }

    @Override
    public List<ShortTask> getAllShort(boolean test) {
        return jdbi.withHandle(
                db -> db.select(getAllQuery(test)).map(new ShortTaskRowMapper()).list() // TODO do not read full description from db
        );
    }

    private String getAllQuery(boolean test) {
        String query = format("SELECT * FROM %s WHERE enabled = true", table);
        final String testClause = " AND test = false";
        if (!test) {
            query += testClause;
        }
        query += " ORDER BY `created` DESC";
        return query;
    }

    @Override
    public Optional<Task> get(String id) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? AND enabled = true", table), id)
                        .map(new TaskRowMapper()).findOne()
        );
    }

    @Override
    public Optional<ShortTask> getShort(String id) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? AND enabled = true", table), id) // TODO do not read full description from DB.
                        .map(new ShortTaskRowMapper()).findOne()
        );
    }

    @Override
    public void delete(String id) {
        jdbi.withHandle(
                db -> db.execute(format("UPDATE %s SET enabled = false WHERE task = ? AND enabled = true", table), id)
        );
    }

    @Override
    public Task update(Task updateRequest) {
        Task t = get(updateRequest.getId()).orElseThrow(RuntimeException::new);
        setFields(t, updateRequest);
        // Disabling old entry
        jdbi.withHandle(db -> db.execute(format("UPDATE %s SET enabled = false WHERE task = ? AND enabled = true", table), t.getId()));
        // Creating new entry
        return create(t);
    }

    @Override
    public Task create(Task t) {
        long now = Instant.now().getEpochSecond();
        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (task, type, name, image_url, short_description, description, description_url, requirements, input, tags, languages, participants, users, offers, bestOffer, created, last_submit, submittable, test, enabled, company, city, job, job_url, emails) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, ?, ?, ?, ?, ?)", table),
                        t.getId(),
                        t.getType().name().toLowerCase(),
                        t.getName(),
                        t.getImageUrl(),
                        t.getShortDescription(),
                        t.getDescription(),
                        t.getDescriptionUrl(),
                        t.getRequirements(),
                        t.getInputFilesUrl(),
                        String.join(",", t.getTags()),
                        EnumUtils.stringify(t.getLanguages()),
                        Optional.ofNullable(t.getParticipants()).orElse(0),
                        Optional.ofNullable(t.getUsers()).orElse(0),
                        Optional.ofNullable(t.getOffers()).orElse(0),
                        Optional.ofNullable(t.getBestOffer()).orElse(0),
                        now,
                        Optional.ofNullable(t.getLastSubmit()).map(Instant::getEpochSecond).orElse(null),
                        t.isSubmittable(),
                        t.isTest(),
                        t.getCompany(),
                        t.getCity(),
                        t.getJob(),
                        t.getJobUrl(),
                        String.join(",", t.getEmails())
                )
        );

        return get(t.getId()).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Task> getHistory(String taskId) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? ORDER BY created DESC", table), taskId)
                        .map(new TaskRowMapper()).list()
        );
    }

    @Override
    public List<Task> getForCompany(String company, boolean enabledOnly) {
        String sql = "SELECT * FROM %s WHERE company = ? AND test = false";
        String enabledClause;
        if (enabledOnly) {
            enabledClause = " AND enabled = true";
        } else {
            enabledClause = "";
        }
        String orderByClause = " ORDER BY created DESC";
        return jdbi.withHandle(
                db -> db.select(format(sql + enabledClause + orderByClause, table), company)
                        .map(new TaskRowMapper()).list()
        );
    }

    private void setFields(Task task, Task t) {
        Optional.ofNullable(t.getType()).ifPresent(task::setType);
        Optional.ofNullable(t.getDescription()).ifPresent(task::setDescription);
        Optional.ofNullable(t.getDescriptionUrl()).ifPresent(task::setDescriptionUrl);
        Optional.ofNullable(t.getInputFilesUrl()).ifPresent(task::setInputFilesUrl);
        Optional.ofNullable(t.getRequirements()).ifPresent(task::setRequirements);
        Optional.ofNullable(t.getName()).ifPresent(task::setName);
        Optional.ofNullable(t.getImageUrl()).ifPresent(task::setImageUrl);
        Optional.ofNullable(t.getShortDescription()).ifPresent(task::setShortDescription);
        Optional.ofNullable(t.getParticipants()).ifPresent(task::setParticipants);
        Optional.ofNullable(t.getUsers()).ifPresent(task::setUsers);
        Optional.ofNullable(t.getOffers()).ifPresent(task::setOffers);
        Optional.ofNullable(t.getBestOffer()).ifPresent(task::setBestOffer);
        Optional.ofNullable(t.getLastSubmit()).ifPresent(task::setLastSubmit);
        Optional.ofNullable(t.getTags()).ifPresent(task::setTags);
        Optional.ofNullable(t.getLanguages()).ifPresent(task::setLanguages);
        Optional.ofNullable(t.getCompany()).ifPresent(task::setCompany);
        Optional.ofNullable(t.getJob()).ifPresent(task::setJob);
        Optional.ofNullable(t.getJobUrl()).ifPresent(task::setJobUrl);
        Optional.ofNullable(t.getCity()).ifPresent(task::setCity);
        Optional.ofNullable(t.getEmails()).ifPresent(task::setEmails);
    }

    private Set<String> stringToSet(String tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(tags.split(",")));
    }

    private Set<Language> langsFromString(String langs) {
        return Optional.ofNullable(langs)
                .filter(t -> !t.trim().isEmpty())
                .map(String::toUpperCase)
                .map(s -> s.split(","))
                .map(t -> Arrays.stream(t).map(Language::valueOf).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    private class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task map(ResultSet rs, StatementContext ctx) throws SQLException {
            Task task = new Task();

            task
                    .setDescription(rs.getString("description"))
                    .setDescriptionUrl(rs.getString("description_url"))
                    .setInputFilesUrl(rs.getString("input"))
                    .setEmails(Optional.ofNullable(rs.getString("emails")).map(JdbiTaskRepository.this::stringToSet).orElseGet(HashSet::new))
                    .setRequirements(rs.getString("requirements"))
                    .setJob(rs.getString("job"))
                    .setJobUrl(rs.getString("job_url"))
                    .setCompany(rs.getString("company"))
                    .setCity(rs.getString("city"))
                    .setLanguages(langsFromString(rs.getString("languages")))
                    .setId(rs.getString("task"))
                    .setName(rs.getString("name"))
                    .setShortDescription(rs.getString("short_description"))
                    .setImageUrl(rs.getString("image_url"))
                    .setParticipants(rs.getInt("participants"))
                    .setUsers(rs.getInt("users"))
                    .setOffers(rs.getInt("offers"))
                    .setBestOffer(rs.getInt("bestOffer"))
                    .setSubmittable(rs.getBoolean("submittable"))
                    .setEnabled(rs.getBoolean("enabled"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")))
                    .setLastSubmit(Optional.ofNullable(rs.getString("last_submit")).map(Long::valueOf).map(Instant::ofEpochSecond).orElse(null))
                    .setTest(rs.getBoolean("test"))
                    .setTags(Optional.ofNullable(rs.getString("tags")).map(String::toUpperCase).map(JdbiTaskRepository.this::stringToSet).orElseGet(HashSet::new))
                    .setType(TaskType.of(rs.getString("type")));
            return task;
        }
    }

    private class ShortTaskRowMapper implements RowMapper<ShortTask> {
        @Override
        public ShortTask map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new ShortTask()
                    .setId(rs.getString("task"))
                    .setName(rs.getString("name"))
                    .setShortDescription(rs.getString("short_description"))
                    .setLanguages(langsFromString(rs.getString("languages")))
                    .setImageUrl(rs.getString("image_url"))
                    .setParticipants(rs.getInt("participants"))
                    .setUsers(rs.getInt("users"))
                    .setOffers(rs.getInt("offers"))
                    .setBestOffer(rs.getInt("bestOffer"))
                    .setSubmittable(rs.getBoolean("submittable"))
                    .setEnabled(rs.getBoolean("enabled"))
                    .setCreated(Instant.ofEpochSecond(rs.getLong("created")))
                    .setLastSubmit(Optional.ofNullable(rs.getString("last_submit")).map(Long::valueOf).map(Instant::ofEpochSecond).orElse(null))
                    .setTest(rs.getBoolean("test"))
                    .setTags(stringToSet(rs.getString("tags").toUpperCase()))
                    .setCompany(rs.getString("company"))
                    .setCity(rs.getString("city"))
                    .setType(TaskType.of(rs.getString("type")));
        }
    }
}
