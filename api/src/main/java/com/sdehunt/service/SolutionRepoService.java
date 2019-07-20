package com.sdehunt.service;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.model.Permission;
import com.sdehunt.commons.model.Template;
import com.sdehunt.commons.model.User;
import com.sdehunt.repository.SolutionRepoRepository;
import com.sdehunt.repository.TemplateRepository;
import com.sdehunt.repository.UserRepository;

public class SolutionRepoService {

    private final static String SOLUTIONS_REPO_OWNER = "BetterHireMe";

    private final TemplateRepository templates;
    private final UserRepository users;
    private final GithubClient githubClient;
    private final SolutionRepoRepository solutionRepos;

    public SolutionRepoService(
            TemplateRepository templates,
            UserRepository users,
            GithubClient githubClient,
            SolutionRepoRepository solutionRepos
    ) {
        this.templates = templates;
        this.users = users;
        this.githubClient = githubClient;
        this.solutionRepos = solutionRepos;
    }

    public String createSolutionRepo(TaskID taskID, String userId, String webhookUrl) {

        Template template = templates.find(taskID).orElseThrow();
        User user = users.get(userId).orElseThrow();
        String repoName = template.getRepo() + "_" + user.getGithubLogin();

        githubClient.copyRepo(
                SOLUTIONS_REPO_OWNER + "/" + template.getRepo(),
                SOLUTIONS_REPO_OWNER,
                repoName,
                getDescription(user.getGithubLogin(), taskID),
                true
        );
        // TODO readme personalization

        String webhookSecret = null;
        githubClient.createWebhook(SOLUTIONS_REPO_OWNER + "/" + repoName, webhookUrl, webhookSecret);

        solutionRepos.save(taskID, userId, SOLUTIONS_REPO_OWNER + "/" + repoName, webhookSecret);

        return githubClient.invite(SOLUTIONS_REPO_OWNER + "/" + repoName, user.getGithubLogin(), Permission.PUSH);
    }

    private String getDescription(String githubLogin, TaskID taskID) {
        return String.format("%s solution for %s task", githubLogin, taskID);
    }
}
