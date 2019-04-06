package com.sdehunt.controller;

import com.sdehunt.model.Solution;
import com.sdehunt.model.impl.SolutionImpl;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.impl.SolutionQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SolutionController {

    @Autowired
    private SolutionRepository solutions;

    @RequestMapping(method = RequestMethod.POST, path = "/tasks/{taskId}/solutions", produces = MediaType.APPLICATION_JSON_VALUE)
    public String save(@PathVariable String taskId, @RequestBody SolutionImpl solution) {
        return solutions.save(solution.withTaskId(taskId));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
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
