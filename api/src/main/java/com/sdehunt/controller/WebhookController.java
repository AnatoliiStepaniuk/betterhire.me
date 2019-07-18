package com.sdehunt.controller;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    @Autowired
    private SolutionService solutionService;

    @RequestMapping(method = RequestMethod.POST, path = "/webhooks/github/solution/push")
    public void userSolutionPush() { // TODO receive object
        // TODO check key if present

        String userId = ""; // TODO
        String repo = ""; // TODO
        String commit = ""; // TODO
        String taskId = ""; // TODO

        Solution solution = new Solution()
                .setUserId(userId)
                .setRepo(repo)
                .setCommit(commit)
                .setTaskId(TaskID.of(taskId))
                .setTest(false);

        solutionService.process(solution);
    }

}
