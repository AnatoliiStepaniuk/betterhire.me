package com.sdehunt.controller;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Language;
import com.sdehunt.dto.RepoResponseDTO;
import com.sdehunt.exception.UserNotFoundException;
import com.sdehunt.repository.SolutionRepoRepository;
import com.sdehunt.security.CurrentUser;
import com.sdehunt.security.UserPrincipal;
import com.sdehunt.service.SolutionRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
    public RepoResponseDTO getRepoForUserTask(
            @PathVariable("taskId") String taskIdRaw,
            @CurrentUser UserPrincipal currentUser,
            @RequestParam("lang") Language language
    ) {
        TaskID taskID = TaskID.of(taskIdRaw);
        String userId = Optional.ofNullable(currentUser).map(UserPrincipal::getId).orElseThrow(UserNotFoundException::new);
        String webhookUrl = host + ":" + port + GITHUB_HOOK_PATH;

        String repoUrl = solutionRepos.find(taskID, userId)
                .map(r -> GITHUB_DOMAIN + r.getRepo())
                .orElseGet(() -> solutionRepoService.createSolutionRepo(taskID, language, userId, webhookUrl));

        return new RepoResponseDTO().setRepoUrl(repoUrl);
    }

}
