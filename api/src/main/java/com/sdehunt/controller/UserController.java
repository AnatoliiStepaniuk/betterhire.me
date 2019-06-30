package com.sdehunt.controller;

import com.sdehunt.commons.model.BestUserResult;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.User;
import com.sdehunt.dto.CreateUserDTO;
import com.sdehunt.exception.UserNotFoundException;
import com.sdehunt.repository.BestSolutionRepository;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.UserQuery;
import com.sdehunt.repository.UserRepository;
import com.sdehunt.security.CurrentUser;
import com.sdehunt.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private SolutionRepository solutions;

    @Autowired
    private BestSolutionRepository bestSolutions;

    @Autowired
    private UserRepository users;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return users.get(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User id " + userPrincipal.getId() + " is not found"));
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Collection<User> getAll(@RequestParam(required = false) boolean test) {
        return users.getAll(test);
    }

    @RequestMapping(path = "/query", method = RequestMethod.POST)
    public Collection<User> query(@RequestBody UserQuery query) { // TODO create tests for all combinations
        return users.query(query);
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public User create(@RequestBody CreateUserDTO req) { // TODO maybe use user instead of DTO?
        User user = new User()
                .setEmail(req.getEmail())
                .setGithubLogin(req.getGithubLogin())
                .setLinkedinId(req.getLinkedinId())
                .setNickname(req.getNickname())
                .setImageUrl(req.getImageUrl())
                .setTest(req.isTest());

        return users.create(user);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public User get(@PathVariable("userId") String userId) {
        return users.get(userId).orElseThrow(UserNotFoundException::new);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public User update(@PathVariable("userId") String userId, @RequestBody CreateUserDTO updateUserRequest) {
        User user = new User()
                .setId(userId)
                .setEmail(updateUserRequest.getEmail())
                .setGithubLogin(updateUserRequest.getGithubLogin())
                .setLinkedinId(updateUserRequest.getLinkedinId())
                .setNickname(updateUserRequest.getNickname())
                .setImageUrl(updateUserRequest.getImageUrl());

        return users.update(user);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("userId") String userId) {
        users.delete(userId);
    }

    @RequestMapping(path = "/{userId}/solutions", method = RequestMethod.GET)
    public List<Solution> userSolutions(@PathVariable("userId") String userId, @RequestParam(required = false) boolean test) {
        return solutions.forUser(userId, test);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{userId}/solutions/best", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BestUserResult> getBestSolutionsForTask(
            @PathVariable("userId") String userId,
            @RequestParam(value = "test", required = false) boolean test

    ) {
        return bestSolutions.bestUserResults(userId, test);
    }

}
