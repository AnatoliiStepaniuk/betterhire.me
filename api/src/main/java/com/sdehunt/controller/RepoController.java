package com.sdehunt.controller;

import com.sdehunt.commons.TaskID;
import com.sdehunt.exception.UserNotFoundException;
import com.sdehunt.repository.SolutionRepoRepository;
import com.sdehunt.security.CurrentUser;
import com.sdehunt.security.UserPrincipal;
import com.sdehunt.service.SolutionRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

import static com.sdehunt.controller.WebhookController.GITHUB_HOOK_PATH;

@RestController
public class RepoController {

    @Value("${BACK_END_HOST}")
    private String host;
    @Value("${BACK_END_PORT}")
    private int port;
    @Autowired
    private SolutionRepoRepository solutionRepos;

    @Autowired
    private SolutionRepoService solutionRepoService;

    private final String GITHUB_DOMAIN = "https://github.com/";

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{taskId}/repo")
    public ResponseEntity<Object> getRepoForUserTask(@PathVariable("taskId") String taskIdRaw, @CurrentUser UserPrincipal currentUser) {
        TaskID taskID = TaskID.of(taskIdRaw);
        String userId = Optional.ofNullable(currentUser).map(UserPrincipal::getId).orElseThrow(UserNotFoundException::new);
        String webhookUrl = host + ":" + port + GITHUB_HOOK_PATH;

        String repoUrl = solutionRepos.find(taskID, userId)
                .map(r -> GITHUB_DOMAIN + r.getRepo())
                .orElseGet(() -> solutionRepoService.createSolutionRepo(taskID, userId, webhookUrl));

        return ResponseEntity.status(301).location(URI.create(repoUrl)).build();
    }

}
