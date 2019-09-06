package com.sdehunt.controller;

import com.sdehunt.commons.model.*;
import com.sdehunt.dto.CreateUserDTO;
import com.sdehunt.exception.UserNotFoundException;
import com.sdehunt.repository.*;
import com.sdehunt.security.CurrentUser;
import com.sdehunt.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private SolutionRepository solutions;

    @Autowired
    private BestSolutionRepository bestSolutions;

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private ReviewRepository reviewsRepo;

    @Autowired
    private SolutionRepository solutionsRepo;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return usersRepo.get(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User id " + userPrincipal.getId() + " is not found"));
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Collection<User> getAll(@RequestParam(required = false) boolean test) {
        return usersRepo.getAll(test);
    }

    @RequestMapping(path = {"/extended", "/reviews"}, method = RequestMethod.GET)
    public List<ExtendedUser> getAllUsersExtended(@RequestParam(required = false) boolean test) {
        Collection<User> users = usersRepo.getAll(test);
        Set<String> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
        Map<String, List<Review>> reviews = reviewsRepo.forUsers(userIds);
        Map<String, List<String>> repos = solutionsRepo.getAllRepos();
        return users.stream().map(
                u -> new ExtendedUser(u)
                        .setReviews(reviews.containsKey(u.getId()) ? reviews.get(u.getId()) : new ArrayList<>())
                        .setRepos(repos.containsKey(u.getId()) ? repos.get(u.getId()) : new ArrayList<>())
        ).collect(Collectors.toList());
    }

    @RequestMapping(path = "/query", method = RequestMethod.POST)
    public Collection<User> query(@RequestBody UserQuery query) { // TODO create tests for all combinations
        return usersRepo.query(query);
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public User create(@RequestBody CreateUserDTO req) {
        User user = new User()
                .setEmail(req.getEmail())
                .setGithubLogin(req.getGithubLogin())
                .setLinkedinId(req.getLinkedinId())
                .setNickname(req.getNickname())
                .setImageUrl(req.getImageUrl())
                .setCv(req.getCv())
                .setPhone(req.getPhone())
                .setCity(req.getCity())
                .setLanguages(req.getLanguages())
                .setTest(req.isTest());

        return usersRepo.create(user);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public User get(@PathVariable("userId") String userId) {
        return usersRepo.get(userId).orElseThrow(UserNotFoundException::new);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public User update(@PathVariable("userId") String userId, @RequestBody CreateUserDTO req) {
        User user = new User()
                .setId(userId)
                .setEmail(req.getEmail())
                .setGithubLogin(req.getGithubLogin())
                .setLinkedinId(req.getLinkedinId())
                .setNickname(req.getNickname())
                .setImageUrl(req.getImageUrl())
                .setCv(req.getCv())
                .setCity(req.getCity())
                .setLanguages(req.getLanguages())
                .setPhone(req.getPhone());

        return usersRepo.update(user);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("userId") String userId) {
        usersRepo.delete(userId);
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
