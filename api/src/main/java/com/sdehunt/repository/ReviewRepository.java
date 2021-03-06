package com.sdehunt.repository;

import com.sdehunt.commons.model.Review;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReviewRepository {

    void create(String solutionId, String userId, String taskId, Long grade, String comment, String emoji, String reviewer);

    List<Review> forUser(String userId);

    Map<String, List<Review>> forUsers(Set<String> userIds);

    List<Review> forUserAndTask(String userId, String taskId);

}
