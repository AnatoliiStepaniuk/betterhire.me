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

    private final static String UPDATED = "UPDATED";

    private final static Random random = new Random();

    @Test
    public void updateTaskTest() throws InterruptedException {

        String taskId = UUID.randomUUID().toString();
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
        String tag = UUID.randomUUID().toString().toUpperCase();
        String email = UUID.randomUUID().toString() + "@" + UUID.randomUUID().toString();
        Language language = Language.values()[random.nextInt(Language.values().length - 1)]; // So that OTHER is not included
        Set<String> tags = Collections.singleton(tag);
        Set<String> emails = Collections.singleton(email);
        Set<Language> languages = Collections.singleton(language);

        Task task = new Task();
        task.setId(taskId);
        task.setDescription(description);
        task.setDescriptionUrl(descriptionUrl);
        task.setRequirements(requirements);
        task.setInputFilesUrl(inputFilesUrl);
        task.setEmails(emails);
        task.setJob(job);
        task.setJobUrl(jobUrl);
        task.setName(name);
        task.setShortDescription(shortDescription);
        task.setTags(tags);
        task.setCity(city);
        task.setLanguages(languages);
        task.setCompany(company);
        task.setTest(true);
        task.setType(TaskType.MANUAL);

        // Create task
        host().body(task)
                .contentType(APP_JSON)
                .post(TASKS)
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
                .body("city", is(city))
                .body("job", is(job))
                .body("jobUrl", is(jobUrl))
                .body("test", is(true))
                .body("tags", contains(tag))
                .body("emails", contains(email))
                .body("type", is(TaskType.MANUAL.name()));

        UpdateTaskDTO taskForUpdate = new UpdateTaskDTO()
                .setDescription(description + UPDATED)
                .setDescriptionUrl(descriptionUrl + UPDATED)
                .setShortDescription(shortDescription + UPDATED)
                .setName(name + UPDATED)
                .setRequirements(requirements + UPDATED)
                .setInputFilesUrl(inputFilesUrl + UPDATED)
                .setTags(Collections.singleton(UPDATED))
                .setEmails(Collections.singleton(UPDATED))
                .setLanguages(Collections.singleton(Language.OTHER))
                .setCompany(company + UPDATED)
                .setCity(city + UPDATED)
                .setJob(job + UPDATED)
                .setJobUrl(jobUrl + UPDATED)
                .setType(TaskType.AUTO);

        Thread.sleep(1000); // Because of sorting by timestamp seconds

        // Updating task
        host()
                .body(taskForUpdate)
                .contentType(APP_JSON)
                .put(TASKS + "/" + taskId)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalToIgnoringCase(taskId))
                .body("description", is(description + UPDATED))
                .body("descriptionUrl", is(descriptionUrl + UPDATED))
                .body("shortDescription", is(shortDescription + UPDATED))
                .body("name", is(name + UPDATED))
                .body("requirements", is(requirements + UPDATED))
                .body("inputFilesUrl", is(inputFilesUrl + UPDATED))
                .body("company", is(company + UPDATED))
                .body("city", is(city + UPDATED))
                .body("languages", contains(Language.OTHER.name()))
                .body("job", is(job + UPDATED))
                .body("jobUrl", is(jobUrl + UPDATED))
                .body("test", is(true))
                .body("tags", contains(UPDATED))
                .body("emails", contains(UPDATED))
                .body("type", is(TaskType.AUTO.name()));

        // Verify updated
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId + SHORT)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalToIgnoringCase(taskId))
                .body("description", isEmptyOrNullString())
                .body("descriptionUrl", isEmptyOrNullString())
                .body("shortDescription", is(shortDescription + UPDATED))
                .body("company", is(company + UPDATED))
                .body("city", is(city + UPDATED))
                .body("name", is(name + UPDATED))
                .body("test", is(true))
                .body("tags", contains(UPDATED))
                .body("type", is(TaskType.AUTO.name()));

        // Check history
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId + HISTORY)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("[0].id", equalToIgnoringCase(taskId))
                .body("[0].description", is(description + UPDATED))
                .body("[0].descriptionUrl", is(descriptionUrl + UPDATED))
                .body("[0].shortDescription", is(shortDescription + UPDATED))
                .body("[0].name", is(name + UPDATED))
                .body("[0].requirements", is(requirements + UPDATED))
                .body("[0].inputFilesUrl", is(inputFilesUrl + UPDATED))
                .body("[0].company", is(company + UPDATED))
                .body("[0].city", is(city + UPDATED))
                .body("[0].job", is(job + UPDATED))
                .body("[0].jobUrl", is(jobUrl + UPDATED))
                .body("[0].test", is(true))
                .body("[0].tags", contains(UPDATED))
                .body("[0].emails", contains(UPDATED))
                .body("[0].type", is(TaskType.AUTO.name()))
                .body("[1].id", equalToIgnoringCase(taskId))
                .body("[1].description", is(description))
                .body("[1].descriptionUrl", is(descriptionUrl))
                .body("[1].shortDescription", is(shortDescription))
                .body("[1].name", is(name))
                .body("[1].requirements", is(requirements))
                .body("[1].inputFilesUrl", is(inputFilesUrl))
                .body("[1].company", is(company))
                .body("[1].city", is(city))
                .body("[1].job", is(job))
                .body("[1].jobUrl", is(jobUrl))
                .body("[1].test", is(true))
                .body("[1].tags", contains(tag))
                .body("[1].emails", contains(email))
                .body("[1].type", is(TaskType.MANUAL.name()));


        // Getting all tasks
        Task[] tasks = host().get(TASKS + "?test=true")
                .as(Task[].class);

        Task foundTask = Arrays.stream(tasks)
                .filter(t -> t.getId().equalsIgnoreCase(taskId))
                .findFirst()
                .orElseThrow();
        Assert.assertEquals(description + UPDATED, foundTask.getDescription());
        Assert.assertEquals(descriptionUrl + UPDATED, foundTask.getDescriptionUrl());
        Assert.assertEquals(requirements + UPDATED, foundTask.getRequirements());
        Assert.assertEquals(inputFilesUrl + UPDATED, foundTask.getInputFilesUrl());
        Assert.assertEquals(Collections.singleton(UPDATED), foundTask.getTags());
        Assert.assertEquals(Collections.singleton(UPDATED), foundTask.getEmails());
        Assert.assertEquals(company + UPDATED, foundTask.getCompany());
        Assert.assertEquals(city + UPDATED, foundTask.getCity());
        Assert.assertEquals(job + UPDATED, foundTask.getJob());
        Assert.assertEquals(jobUrl + UPDATED, foundTask.getJobUrl());
        Assert.assertEquals(TaskType.AUTO, foundTask.getType());

        ShortTask[] shortTasks = host().get(TASKS + SHORT + "?test=true")
                .as(ShortTask[].class);
        ShortTask foundShortTask = Arrays.stream(shortTasks)
                .filter(t -> t.getId().equalsIgnoreCase(taskId))
                .findFirst()
                .orElseThrow();
        Assert.assertEquals(shortDescription + UPDATED, foundShortTask.getShortDescription());
        Assert.assertEquals(TaskType.AUTO, foundShortTask.getType());

        host().contentType(APP_JSON)
                .delete(TASKS + "/" + taskId)
                .then()
                .log().ifValidationFails()
                .statusCode(NO_CONTENT)
                .body(isEmptyString());

        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId)
                .then()
                .log().ifValidationFails()
                .statusCode(NOT_FOUND);
    }
}
