package com.sdehunt.repository;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Review;

import java.util.List;

public interface ReviewRepository {

    void create(String solutionId, String userId, TaskID taskID, Long grade, String comment, String emoji, String reviewer);

    List<Review> forUser(String userId);

    List<Review> forUserAndTask(String userId, TaskID taskID);

}
