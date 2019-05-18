package com.sdehunt.service.solution;

import com.sdehunt.commons.GithubClient;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.score.GeneralScoreCounter;

public class SolutionService {

    private GeneralScoreCounter scoreCounter;

    private SolutionRepository solutionRepository;

    private GithubClient githubClient;

    public SolutionService(
            GeneralScoreCounter scoreCounter,
            SolutionRepository solutionRepository,
            GithubClient githubClient
    ) {
        this.scoreCounter = scoreCounter;
        this.solutionRepository = solutionRepository;
        this.githubClient = githubClient;
    }

    public long calculateScoreAndSave(Solution solution) {

        if (solution.getCommit().equalsIgnoreCase("master")) { // TODO better create method isBranch
            String commit = githubClient.getCommit(solution.getRepo(), solution.getCommit());
            solution.setCommit(commit);
        }

        long score = scoreCounter.count(solution);
        solutionRepository.save(solution.setScore(score)); // TODO resolve commit (if master)
        return score;
    }
}
