package com.sdehunt.service;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.BestSolution;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.User;
import com.sdehunt.repository.BestSolutionRepository;
import com.sdehunt.repository.TaskRepository;
import com.sdehunt.repository.UserRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class BestSolutionService {

    private final BestSolutionRepository bestSolutions;
    private final UserRepository users;
    private final TaskRepository tasks;

    public BestSolutionService(BestSolutionRepository bestSolutions, UserRepository users, TaskRepository tasks) {
        this.bestSolutions = bestSolutions;
        this.users = users;
        this.tasks = tasks;
    }

    public void updateIfNeeded(Solution s, long score) {
        // Getting all best solutions for task
        List<BestSolution> solutions = bestSolutions.getForTask(s.getTaskId(), s.isTest());
        // Updating score for user or adding new entry if it is his first attempt
        Optional<BestSolution> betterSolution = updateUserSolutionIfBetter(solutions, s, score);
        // Updating number of users that solved this task
        updateTask(s.getTaskId(), solutions);

        if (betterSolution.isEmpty()) {
            return;
        }
        // Sorting by score to define new order
        solutions.sort(Comparator.comparingLong(BestSolution::getScore).reversed());
        // Updating rank based on new order and getting entries with changed rank or score(for updating)
        Set<BestSolution> solutionsToUpdate = updateRank(solutions);
        solutionsToUpdate.add(betterSolution.get()); // Adding better solution manually because method updateRank returns only solutions with rank changed (although rank can be the same even if score increased).
        // Updating entries with changed rank
        bestSolutions.save(solutionsToUpdate);
        // Updating average rank for users with changed rank for this task
        solutionsToUpdate.forEach(bs -> updateUser(bs.getUserId()));
    }

    private void updateTask(TaskID taskID, List<BestSolution> solutions) {
        int usersCount = solutions.stream().collect(Collectors.groupingBy(BestSolution::getUserId)).size();
        Task task = new Task();
        task.setId(taskID);
        task.setUsers(usersCount);
        task.setLastSubmit(Instant.now());
        tasks.update(task);
    }

    private int getRank(int position, int total) {
        return (int) (100.0 * position / total) + 1;
    }

    private Optional<BestSolution> updateUserSolutionIfBetter(List<BestSolution> solutions, Solution solution, long score) {
        Optional<BestSolution> found = solutions.stream()
                .filter(s -> s.getTaskID() == solution.getTaskId() && s.getUserId().equals(solution.getUserId()))
                .findAny();
        BestSolution bestSolution = null;
        if (found.isPresent()) {
            if (score > found.get().getScore()) {
                bestSolution = found.get().setScore(score).setSolutionId(solution.getId());
            }
        } else {
            bestSolution = new BestSolution()
                    .setTaskID(solution.getTaskId())
                    .setUserId(solution.getUserId())
                    .setSolutionId(solution.getId())
                    .setScore(score)
                    .setRank(-1)
                    .setTest(solution.isTest());
            solutions.add(bestSolution);
        }

        return Optional.ofNullable(bestSolution);
    }

    /*
        Updates rank and returns solutions whose rank was changed.
     */
    private Set<BestSolution> updateRank(List<BestSolution> solutions) {
        Set<BestSolution> toUpdate = new HashSet<>();
        for (int i = 0; i < solutions.size(); i++) {
            int rank = getRank(i, solutions.size());
            if (rank != solutions.get(i).getRank()) {
                solutions.get(i).setRank(rank);
                toUpdate.add(solutions.get(i));
            }
        }
        return toUpdate;
    }

    private void updateUser(String userId) {
        User user = users.get(userId).orElseThrow();
        List<BestSolution> bestUserSolutions = bestSolutions.getForUser(userId);
        int avgRank = (int) (double) bestUserSolutions.stream().collect(Collectors.averagingInt(BestSolution::getRank));
        user.setAvgRank(avgRank).setSolved(bestUserSolutions.size());
        users.update(user);
    }

}
