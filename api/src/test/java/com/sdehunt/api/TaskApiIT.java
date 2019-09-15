package com.sdehunt.api;

import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.TaskType;
import com.sdehunt.dto.UpdateTaskDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;


public class TaskApiIT extends AbstractApiTest {

    private final static String TASKS = "/tasks";
    private final static String SHORT = "/short";
    private final static String HISTORY = "/history";

    private final static Random random = new Random();

    @Test
    public void updateTaskTest() {

        String taskId = "slides_test";
        String description = UUID.randomUUID().toString();
        String descriptionUrl = UUID.randomUUID().toString();
        String shortDescription = UUID.randomUUID().toString();
        String name = UUID.randomUUID().toString();
        String requirements = UUID.randomUUID().toString();
        String inputFilesUrl = UUID.randomUUID().toString();
        String company = UUID.randomUUID().toString();
        String city = UUID.randomUUID().toString();
        String job = UUID.randomUUID().toString();
        String jobUrl = UUID.randomUUID().toString();
        TaskType type = TaskType.values()[random.nextInt(TaskType.values().length)];
        String tag = UUID.randomUUID().toString().toUpperCase();
        Language language = Language.values()[random.nextInt(Language.values().length)];
        Set<String> tags = Collections.singleton(tag);
        Set<Language> languages = Collections.singleton(language);

        // Verify task is present
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(not(isEmptyOrNullString()))
                .body("test", is(true));

        UpdateTaskDTO taskForUpdate = new UpdateTaskDTO()
                .setDescription(description)
                .setDescriptionUrl(descriptionUrl)
                .setShortDescription(shortDescription)
                .setName(name)
                .setRequirements(requirements)
                .setInputFilesUrl(inputFilesUrl)
                .setTags(tags)
                .setLanguages(languages)
                .setCompany(company)
                .setCity(city)
                .setJob(job)
                .setJobUrl(jobUrl)
                .setType(type);

        // Updating task
        host()
                .body(taskForUpdate)
                .contentType(APP_JSON)
                .put(TASKS + "/" + taskId)
                .then()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        // Verify updated
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalToIgnoringCase(taskId))
                .body("description", is(description))
                .body("descriptionUrl", is(descriptionUrl))
                .body("shortDescription", is(shortDescription))
                .body("name", is(name))
                .body("requirements", is(requirements))
                .body("inputFilesUrl", is(inputFilesUrl))
                .body("company", is(company))
                .body("city", is(city))
                .body("job", is(job))
                .body("jobUrl", is(jobUrl))
                .body("test", is(true))
                .body("tags", contains(tag))
                .body("type", is(type.name()));

        // Verify updated
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId + SHORT)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalToIgnoringCase(taskId))
                .body("description", isEmptyOrNullString())
                .body("descriptionUrl", isEmptyOrNullString())
                .body("shortDescription", is(shortDescription))
                .body("company", is(company))
                .body("city", is(city))
                .body("name", is(name))
                .body("test", is(true))
                .body("tags", contains(tag))
                .body("type", is(type.name()));

        // Check history
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId + HISTORY)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("[0].id", equalToIgnoringCase(taskId))
                .body("[0].description", is(description))
                .body("[0].descriptionUrl", is(descriptionUrl))
                .body("[0].shortDescription", is(shortDescription))
                .body("[0].name", is(name))
                .body("[0].requirements", is(requirements))
                .body("[0].inputFilesUrl", is(inputFilesUrl))
                .body("[0].company", is(company))
                .body("[0].city", is(city))
                .body("[0].job", is(job))
                .body("[0].jobUrl", is(jobUrl))
                .body("[0].test", is(true))
                .body("[0].tags", contains(tag))
                .body("[0].type", is(type.name()));


        // Getting all tasks
        Task[] tasks = host().get(TASKS + "?test=true")
                .as(Task[].class);

        Task foundTask = Arrays.stream(tasks)
                .filter(t -> t.getId().equalsIgnoreCase(taskId))
                .findFirst()
                .orElseThrow();
        Assert.assertEquals(description, foundTask.getDescription());
        Assert.assertEquals(descriptionUrl, foundTask.getDescriptionUrl());
        Assert.assertEquals(requirements, foundTask.getRequirements());
        Assert.assertEquals(inputFilesUrl, foundTask.getInputFilesUrl());
        Assert.assertEquals(tags, foundTask.getTags());
        Assert.assertEquals(company, foundTask.getCompany());
        Assert.assertEquals(city, foundTask.getCity());
        Assert.assertEquals(job, foundTask.getJob());
        Assert.assertEquals(jobUrl, foundTask.getJobUrl());
        Assert.assertEquals(type, foundTask.getType());

        ShortTask[] shortTasks = host().get(TASKS + SHORT + "?test=true")
                .as(ShortTask[].class);
        ShortTask foundShortTask = Arrays.stream(shortTasks)
                .filter(t -> t.getId().equalsIgnoreCase(taskId))
                .findFirst()
                .orElseThrow();
        Assert.assertEquals(shortDescription, foundShortTask.getShortDescription());
        Assert.assertEquals(type, foundShortTask.getType());
    }
}
