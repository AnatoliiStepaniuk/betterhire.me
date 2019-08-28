package com.sdehunt.controller;

import com.sdehunt.commons.model.Review;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.dto.SaveReviewDTO;
import com.sdehunt.repository.ReviewRepository;
import com.sdehunt.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository reviews;

    @Autowired
    private SolutionRepository solutions;

    @RequestMapping(
            method = RequestMethod.POST, path = "/solutions/{solutionId}/review",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void save(
            @PathVariable String solutionId,
//            @CurrentUser UserPrincipal currentUser, // TODO use it as reviewer
            @RequestBody SaveReviewDTO req
    ) {
        Solution solution = solutions.get(solutionId).orElseThrow();
        reviews.create(solutionId, solution.getUserId(), solution.getTaskId(), req.getGrade(), req.getComment(), req.getEmoji(), "betterhire");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/reviews/user/{userId}")
    public List<Review> get(@PathVariable String userId) {
        return reviews.forUser(userId);
    }

}
