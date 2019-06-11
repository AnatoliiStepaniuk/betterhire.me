package com.sdehunt.controller;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.BestResult;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.dto.SaveSolutionDTO;
import com.sdehunt.dto.SolutionIdDTO;
import com.sdehunt.exception.SolutionNotFoundException;
import com.sdehunt.exception.UserNotFoundException;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.impl.SolutionQueryImpl;
import com.sdehunt.security.CurrentUser;
import com.sdehunt.security.UserPrincipal;
import com.sdehunt.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SolutionController {

    @Autowired
    private SolutionRepository solutions;

    @Autowired
    private SolutionService solutionService;

    @RequestMapping(
            method = RequestMethod.POST, path = "/tasks/{taskId}/solutions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SolutionIdDTO submit(@PathVariable String taskId, @RequestBody SaveSolutionDTO req, @CurrentUser UserPrincipal user) {
        Solution solution = new Solution()
                .setUserId(Optional.ofNullable(user).map(UserPrincipal::getId).orElseThrow(UserNotFoundException::new))
                .setRepo(req.getRepo())
                .setCommit(req.getCommit())
                .setTaskId(TaskID.of(taskId))
                .setTest(req.isTest());

        return new SolutionIdDTO().setId(solutionService.process(solution));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/solutions/{solutionId}")
    public Solution get(@PathVariable String solutionId) {
        return solutions.get(solutionId).orElseThrow(SolutionNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/solutions/{solutionId}")
    public void delete(@PathVariable String solutionId) {
        solutions.delete(solutionId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{taskId}/solutions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Solution> getSolutionsForTask(
            @PathVariable String taskId,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "status", required = false) SolutionStatus status,
            @RequestParam(value = "test", required = false) boolean test
    ) {
        return solutions.query(
                new SolutionQueryImpl().task(taskId).user(userId).status(status).test(test)
        );
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{taskId}/solutions/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Solution> getMySolutionsForTask(
            @PathVariable String taskId,
            @RequestParam(value = "status", required = false) SolutionStatus status,
            @RequestParam(value = "test", required = false) boolean test,
            @CurrentUser UserPrincipal user
    ) {
        String userId = Optional.ofNullable(user).map(UserPrincipal::getId).orElseThrow(UserNotFoundException::new);
        return solutions.query(
                new SolutionQueryImpl().task(taskId).user(userId).status(status).test(test)
        );
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{taskId}/solutions/best", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BestResult> getBestSolutionsForTask(@PathVariable String taskId) { // TODO test
        return solutions.best(taskId);
    }
}
