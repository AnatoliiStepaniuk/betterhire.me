package com.sdehunt.service.solution;

import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.dto.SolutionScoreDTO;
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

    public SolutionScoreDTO calculateScoreAndSave(Solution solution) {

        if (isBranch(solution.getRepo(), solution.getCommit())) {
            String commit = githubClient.getCommit(solution.getRepo(), solution.getCommit());
            solution.setCommit(commit);
        }

        long score = count(solution);

        String solutionId = solutionRepository.save(solution.setScore(score));

        return new SolutionScoreDTO()
                .setSolutionId(solutionId)
                .setScore(score);
    }

    private boolean isBranch(String repo, String input) {
        try {
            return githubClient.isBranch(repo, input);
        } catch (RepositoryNotFoundException e) {
            throw new com.sdehunt.exception.RepositoryNotFoundException(repo);
        }
    }

    private long count(Solution solution) {
        try {
            return scoreCounter.count(solution);
        } catch (CommitOrFileNotFoundException e) {
            throw new com.sdehunt.exception.CommitOrFileNotFoundException();
        }
    }
}
