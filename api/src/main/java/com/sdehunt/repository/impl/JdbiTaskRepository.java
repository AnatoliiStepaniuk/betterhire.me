package com.sdehunt.repository.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.github.JavaGithubClient;
import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Tag;
import com.sdehunt.commons.model.Task;
import com.sdehunt.repository.TaskRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Logger logger;

    public JdbiTaskRepository(DataSource dataSource, String db) {
        this.jdbi = Jdbi.create(dataSource);
        this.table = "`" + db + "`.`task`";
        this.logger = LoggerFactory.getLogger(JavaGithubClient.class);
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
    public Optional<Task> get(TaskID id) {
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
    public void update(Task updateRequest) {
        Task t = get(updateRequest.getId()).orElseThrow();
        setFields(t, updateRequest);
        // Disabling old entry
        jdbi.withHandle(db -> db.execute(format("UPDATE %s SET enabled = false WHERE task = ? AND enabled = true", table), t.getId()));
        // Creating new entry
        long now = Instant.now().getEpochSecond();
        jdbi.withHandle(
                db -> db.execute(
                        format("INSERT INTO %s (task, name, image_url, short_description, description, description_url, requirements, input, tags, languages, participants, users, offers, bestOffer, created, last_submit, submittable, test, enabled, company, job, job_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, ?, ?, ?)", table),
                        t.getId(),
                        t.getName(),
                        t.getImageUrl(),
                        t.getShortDescription(),
                        t.getDescription(),
                        t.getDescriptionUrl(),
                        t.getRequirements(),
                        t.getInputFilesUrl(),
                        stringify(t.getTags()),
                        stringify(t.getLanguages()),
                        t.getParticipants(),
                        t.getUsers(),
                        t.getOffers(),
                        t.getBestOffer(),
                        now,
                        Optional.ofNullable(t.getLastSubmit()).map(Instant::getEpochSecond).orElse(null),
                        t.isSubmittable(),
                        t.isTest(),
                        t.getCompany(),
                        t.getJob(),
                        t.getJobUrl()
                )
        );
    }

    @Override
    public List<Task> getHistory(TaskID taskID) {
        return jdbi.withHandle(
                db -> db.select(format("SELECT * FROM %s WHERE task = ? ORDER BY id DESC", table), taskID)
                        .map(new TaskRowMapper()).list()
        );
    }

    private void setFields(Task task, Task t) {
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
    }

    private String stringify(Set<? extends Enum> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream()
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));
    }

    private Set<Tag> tagsFromString(String tags) { // TODO make one generic method
        return Optional.ofNullable(tags)
                .filter(t -> !t.isEmpty() && !t.isBlank())
                .map(String::toUpperCase)
                .map(s -> s.split(","))
                .map(t -> Arrays.stream(t).map(Tag::valueOf).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    private Set<Language> langsFromString(String langs) {
        return Optional.ofNullable(langs)
                .filter(t -> !t.isEmpty() && !t.isBlank())
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
                    .setRequirements(rs.getString("requirements"))
                    .setJob(rs.getString("job"))
                    .setJobUrl(rs.getString("job_url"))
                    .setCompany(rs.getString("company"))
                    .setLanguages(langsFromString(rs.getString("languages")))
                    .setId(TaskID.of(rs.getString("task")))
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
                    .setTags(tagsFromString(rs.getString("tags")));
            return task;
        }
    }

    private class ShortTaskRowMapper implements RowMapper<ShortTask> {
        @Override
        public ShortTask map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new ShortTask()
                    .setId(TaskID.of(rs.getString("task")))
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
                    .setTags(tagsFromString(rs.getString("tags")))
                    .setCompany(rs.getString("company"));
        }
    }
}
