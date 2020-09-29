package com.sdehunt.controller;

import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionRepo;
import com.sdehunt.commons.model.User;
import com.sdehunt.dto.PushHookDTO;
import com.sdehunt.repository.SolutionRepoRepository;
import com.sdehunt.repository.UserRepository;
import com.sdehunt.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebhookController {

    public final static String GITHUB_HOOK_PATH = "/hooks/github/solution/push";

    private final static String EMPTY_COMMIT_HASH = "0000000000000000000000000000000000000000";

    @Autowired
    private SolutionRepoRepository solutionRepos;

    @Autowired
    private UserRepository users;

    @Autowired
    private SolutionService solutionService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.POST, path = GITHUB_HOOK_PATH)
    public void userSolutionPush(@RequestBody PushHookDTO hook) {
        // TODO check secret key if present
        String repo = hook.getRepository().getFullName();
        if (hook.getCommits() == null || hook.getBefore().equals(EMPTY_COMMIT_HASH)) {
            return; // Ignoring non push events
        }
        String commit = hook.getCommits().get(hook.getCommits().size() - 1).getId();
        SolutionRepo solutionRepo = solutionRepos.find(repo).orElseThrow(RuntimeException::new);
        User user = users.get(solutionRepo.getUserId()).orElseThrow(RuntimeException::new);

        Solution solution = new Solution()
                .setUserId(solutionRepo.getUserId())
                .setRepo(repo)
                .setCommit(commit)
                .setTaskId(solutionRepo.getTaskId())
                .setTest(user.isTest());

        solutionService.process(solution);
    }

}
