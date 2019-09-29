package com.sdehunt.controller;

import com.sdehunt.commons.model.Review;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.User;
import com.sdehunt.dto.SaveReviewDTO;
import com.sdehunt.repository.ReviewRepository;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.UserRepository;
import com.sdehunt.repository.impl.SolutionQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository reviews;

    @Autowired
    private SolutionRepository solutions;

    @Autowired
    private UserRepository users;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(
            method = RequestMethod.POST, path = "/solutions/{solutionId}/review",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void save(
            @PathVariable String solutionId,
            @RequestBody SaveReviewDTO req
    ) {
        Solution solution = solutions.get(solutionId).orElseThrow();
        reviews.create(solutionId, solution.getUserId(), solution.getTaskId(), req.getGrade(), req.getComment(), req.getEmoji(), "betterhire");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(
            method = RequestMethod.POST, path = "/user/github/{github}/review/last",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void reviewLastSolutionByGithubLogin(
            @PathVariable String github,
            @RequestBody SaveReviewDTO req
    ) {
        User user = users.byGithubLogin(github).orElseThrow();
        Solution solution = solutions.query(new SolutionQueryImpl().user(user.getId())).get(0);
        reviews.create(solution.getId(), solution.getUserId(), solution.getTaskId(), req.getGrade(), req.getComment(), req.getEmoji(), "betterhire");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/reviews/user/{userId}")
    public List<Review> get(@PathVariable String userId) {
        return reviews.forUser(userId);
    }

}
