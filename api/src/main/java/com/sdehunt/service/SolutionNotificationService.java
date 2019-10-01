package com.sdehunt.service;

import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.User;
import com.sdehunt.repository.SolutionRepository;

import java.util.HashMap;
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
        if (isFirstSolution(user.getId(), task.getId())) {
            int totalSolutions = solutionRepository.getNumberUsersSolvedTask(task.getId());
            Map<String, Object> params = new HashMap<>();
            params.put("taskName", task.getName());
            params.put("url", "https://betterhire.me/auth_manager?company=" + task.getCompany());
            params.put("count", totalSolutions);
            task.getEmails().forEach(e -> emailService.send(e, TEMPLATE, params));
        }
    }

    private boolean isFirstSolution(String userId, String taskId) {
        return solutionRepository.forUserAndTask(userId, taskId).size() == 1;
    }

}
