package com.sdehunt.service;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.BestSolution;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.User;
import com.sdehunt.repository.BestSolutionRepository;
import com.sdehunt.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BestSolutionService {

    private final BestSolutionRepository bestSolutions;
    private final UserRepository users;

    public BestSolutionService(BestSolutionRepository bestSolutions, UserRepository users) {
        this.bestSolutions = bestSolutions;
        this.users = users;
    }

    public void updateIfNeeded(Solution s, long score) {
        // Getting all best solutions for task
        List<BestSolution> solutions = bestSolutions.getForTask(s.getTaskId(), s.isTest());
        // Updating score for user or adding new entry if it is his first attempt
        updateScoreForUser(solutions, s.getTaskId(), s.getUserId(), score, s.isTest());
        // Sorting by score to define new order
        solutions.sort(Comparator.comparingLong(BestSolution::getScore).reversed());
        // Updating rank based on new order and getting entries with changed rank (for updating)
        List<BestSolution> solutionsToUpdate = updateRank(solutions);
        // Updating entries with changed rank
        bestSolutions.save(solutionsToUpdate);
        // Updating average rank for users with changed rank for this task
        solutionsToUpdate.forEach(bs -> updateAvgRank(bs.getUserId()));
    }

    private int getRank(int position, int total) {
        return (int) (100.0 * position / total) + 1;
    }

    private void updateScoreForUser(List<BestSolution> solutions, TaskID taskId, String userId, long score, boolean test) {
        Optional<BestSolution> found = solutions.stream()
                .filter(s -> s.getTaskID() == taskId && s.getUserId().equals(userId))
                .findAny();
        if (found.isPresent()) {
            found.get().setScore(score);
        } else {
            solutions.add(new BestSolution()
                    .setTaskID(taskId)
                    .setUserId(userId)
                    .setScore(score)
                    .setRank(-1)
                    .setTest(test));
        }
    }

    /*
        Updates rank and returns solutions whose rank was changed.
     */
    private List<BestSolution> updateRank(List<BestSolution> solutions) {
        List<BestSolution> toUpdate = new ArrayList<>();
        for (int i = 0; i < solutions.size(); i++) {
            int rank = getRank(i, solutions.size());
            if (rank != solutions.get(i).getRank()) {
                solutions.get(i).setRank(rank);
                toUpdate.add(solutions.get(i));
            }
        }
        return toUpdate;
    }

    private void updateAvgRank(String userId) {
        User user = users.get(userId).orElseThrow();
        List<BestSolution> bestUserSolutions = bestSolutions.getForUser(userId);
        int avgRank = (int) (double) bestUserSolutions.stream().collect(Collectors.averagingInt(BestSolution::getRank));
        user.setAvgRank(avgRank).setSolved(bestUserSolutions.size());
        users.update(user);
    }

}
