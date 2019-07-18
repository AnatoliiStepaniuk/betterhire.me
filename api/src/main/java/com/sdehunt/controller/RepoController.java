package com.sdehunt.controller;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.util.GithubUtils;
import com.sdehunt.exception.UserNotFoundException;
import com.sdehunt.repository.SolutionRepoRepository;
import com.sdehunt.security.CurrentUser;
import com.sdehunt.security.UserPrincipal;
import com.sdehunt.service.SolutionRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RepoController {

    private final static String WEBHOOK_PATH = "/tasks/{taskId}/repo";
    @Value("${BACK_END_HOST}")
    private String host;
    @Value("${BACK_END_PORT}")
    private int port;
    @Autowired
    private SolutionRepoRepository solutionRepos;

    @Autowired
    private SolutionRepoService solutionRepoService;

    @RequestMapping(method = RequestMethod.GET, path = WEBHOOK_PATH)
    public String getRepoForUserTask(@PathVariable String taskIdRaw, @CurrentUser UserPrincipal currentUser) { // TODO return 301 response
        TaskID taskID = TaskID.of(taskIdRaw);
        String userId = Optional.ofNullable(currentUser).map(UserPrincipal::getId).orElseThrow(UserNotFoundException::new);
        String webhookUrl = host + ":" + port + WEBHOOK_PATH;
        return solutionRepos.find(taskID, userId)
                .map(GithubUtils::getInvitationLink)
                .orElseGet(() -> solutionRepoService.createSolutionRepo(taskID, userId, webhookUrl));
    }

}
