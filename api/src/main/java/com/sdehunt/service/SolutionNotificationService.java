package com.sdehunt.service;

import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.User;
import com.sdehunt.repository.SolutionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionNotificationService {

    private final SolutionRepository solutionRepository;
    private final EmailService emailService;

    private final String TEMPLATE = "NewTaskSolution";

    public SolutionNotificationService(SolutionRepository solutionRepository, EmailService emailService) {
        this.solutionRepository = solutionRepository;
        this.emailService = emailService;
    }

    public void notifyOnFirstSolution(Task task, User user) {
        List<Solution> userSolutions = solutionRepository.forUserAndTask(user.getId(), task.getId());
        int totalSolutions = solutionRepository.getNumberUsersSolvedTask(task.getId());
        if (userSolutions.size() == 2) {
            Map<String, Object> params = new HashMap<>();
            params.put("taskName", task.getName());
            params.put("url", "https://betterhire.me/scoreboard_manager"); // TODO fix
            params.put("solved", totalSolutions);
            task.getEmails().forEach(e -> emailService.send(e, TEMPLATE, params));
        }
    }

}
