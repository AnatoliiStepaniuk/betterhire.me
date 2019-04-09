package com.sdehunt.controller;

import com.sdehunt.model.Solution;
import com.sdehunt.model.impl.SolutionImpl;
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

    @RequestMapping(method = RequestMethod.POST, path = "/tasks/{taskId}/solutions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public long save(@PathVariable String taskId, @RequestBody SolutionImpl solution) {
        return solutionService.calculateScoreAndSave(solution.withTaskId(taskId));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/solutions/{id}")
    public Solution get(@PathVariable String id) {
        return solutions.get(id).orElse(null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/solutions/{id}")
    public void delete(@PathVariable String id) {
        solutions.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{taskId}/solutions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Solution> find(
            @PathVariable String taskId,
            @RequestParam(value = "userId", required = false) String userId
    ) {
        return solutions.query(
                new SolutionQueryImpl().withTask(taskId).withUser(userId)
        );
    }
}
