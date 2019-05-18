package com.sdehunt.controller;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.impl.SolutionImpl;
import com.sdehunt.dto.SaveSolutionDTO;
import com.sdehunt.dto.SolutionScoreDTO;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.impl.SolutionQueryImpl;
import com.sdehunt.service.solution.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SolutionController {

    @Autowired
    private SolutionRepository solutions;

    @Autowired
    private SolutionService solutionService;

    // TODO return 400 for invalid request (wrong repo or solutions folder).
    @RequestMapping(
            method = RequestMethod.POST, path = "/tasks/{taskId}/solutions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SolutionScoreDTO save(@PathVariable String taskId, @RequestBody SaveSolutionDTO solutionRequest) {
        Solution solution = SolutionImpl.builder()
                .userId(solutionRequest.getUserId())
                .repo(solutionRequest.getRepo())
                .commit(solutionRequest.getCommit())
                .taskId(TaskID.of(taskId))
                .build();

        return solutionService.calculateScoreAndSave(solution);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/solutions/{solutionId}")
    public Solution get(@PathVariable String solutionId) {
        return solutions.get(solutionId).orElse(null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/solutions/{solutionId}")
    public void delete(@PathVariable String solutionId) {
        solutions.delete(solutionId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{taskId}/solutions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Solution> getSolutionsForTask(
            @PathVariable String taskId,
            @RequestParam(value = "userId", required = false) String userId
    ) {
        return solutions.query(
                new SolutionQueryImpl().withTask(taskId).withUser(userId)
        );
    }
}
