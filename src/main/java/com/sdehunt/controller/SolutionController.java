package com.sdehunt.controller;

import com.sdehunt.model.Solution;
import com.sdehunt.model.impl.SolutionImpl;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.impl.SolutionQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/solutions")
public class SolutionController {

    @Autowired
    private SolutionRepository solutions;

    @RequestMapping(method = RequestMethod.POST, path = "", consumes = "application/json")
    public String save(@PathVariable String taskId, @RequestBody SolutionImpl solution) { // TODO DTO?
        return solutions.save(solution.withTaskId(taskId));
    }

    @RequestMapping(method = RequestMethod.GET, path = "", produces = "application/json")
    public List<Solution> find(
            @PathVariable String taskId,
            @RequestParam(value = "userId", required = false) String userId
    ) {
        return solutions.query(
                new SolutionQueryImpl().withTask(taskId).withUser(userId)
        );
    }
}
