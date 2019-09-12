package com.sdehunt.service;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.ExtendedUser;
import com.sdehunt.commons.model.Review;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.User;
import com.sdehunt.repository.ReviewRepository;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.TaskRepository;
import com.sdehunt.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

public class ExtendedUserService {

    private final UserRepository usersRepo;

    private final ReviewRepository reviewsRepo;

    private final SolutionRepository solutionsRepo;

    private final TaskRepository tasksRepo;

    public ExtendedUserService(
            UserRepository usersRepo,
            ReviewRepository reviewsRepo,
            SolutionRepository solutionsRepo,
            TaskRepository tasksRepo
    ) {
        this.usersRepo = usersRepo;
        this.reviewsRepo = reviewsRepo;
        this.solutionsRepo = solutionsRepo;
        this.tasksRepo = tasksRepo;
    }

    public List<ExtendedUser> getForTask(@RequestParam TaskID taskId) {
        return getForTasks(Collections.singleton(taskId));
    }

    public List<ExtendedUser> getForCompany(@RequestParam String company) {
        List<Task> tasks = tasksRepo.getForCompany(company); // TODO make it as a route
        Set<TaskID> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toSet());
        return getForTasks(taskIds);
    }


    private List<ExtendedUser> getForTasks(Set<TaskID> tasks) {
        Set<String> userIds = solutionsRepo.solvedTasks(tasks);
        Collection<User> users = usersRepo.getByIds(userIds);
        Map<String, List<Review>> reviews = reviewsRepo.forUsers(userIds);
        Map<String, List<String>> repos = solutionsRepo.getTasksRepos(tasks);
        return users.stream().map(
                u -> new ExtendedUser(u)
                        .setReviews(reviews.containsKey(u.getId()) ? reviews.get(u.getId()) : new ArrayList<>())
                        .setRepos(repos.containsKey(u.getId()) ? repos.get(u.getId()) : new ArrayList<>())
        ).collect(Collectors.toList());
    }
}
