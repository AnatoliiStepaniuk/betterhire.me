package com.sdehunt.controller;

import com.sdehunt.commons.model.Solution;
import com.sdehunt.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/user/{userId}")
public class UserController {

    @Autowired
    public SolutionRepository solutions;

    @RequestMapping(path = "/solutions", method = RequestMethod.GET)
    public List<Solution> userSolutions(@PathVariable("userId") String userId) {
        return solutions.forUser(userId);
    }

}
