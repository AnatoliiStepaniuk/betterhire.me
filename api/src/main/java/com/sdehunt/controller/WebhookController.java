package com.sdehunt.controller;

import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionRepo;
import com.sdehunt.dto.PushHookDTO;
import com.sdehunt.repository.SolutionRepoRepository;
import com.sdehunt.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    public final static String GITHUB_HOOK_PATH = "/hooks/github/solution/push";

    @Autowired
    private SolutionRepoRepository solutionRepos;

    @Autowired
    private SolutionService solutionService;

    @RequestMapping(method = RequestMethod.POST, path = GITHUB_HOOK_PATH)
    public void userSolutionPush(@RequestBody PushHookDTO hook) { // TODo avoid NPEs on first call
        // TODO check secret key if present
        String repo = hook.getRepository().getFullName();
        String commit = hook.getCommits().get(hook.getCommits().size() - 1).getId();
        SolutionRepo solutionRepo = solutionRepos.find(repo).orElseThrow();

        Solution solution = new Solution()
                .setUserId(solutionRepo.getUserId())
                .setRepo(repo)
                .setCommit(commit)
                .setTaskId(solutionRepo.getTaskID())
                .setTest(false);

        solutionService.process(solution);
    }

}
