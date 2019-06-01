package com.sdehunt.service.solution;

import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.score.GeneralScoreCounter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SolutionService {

    private GeneralScoreCounter scoreCounter;

    private SolutionRepository solutionRepository;

    private GithubClient githubClient;

    private Executor executor;

    public SolutionService(
            GeneralScoreCounter scoreCounter,
            SolutionRepository solutionRepository,
            GithubClient githubClient
    ) {
        this.scoreCounter = scoreCounter;
        this.solutionRepository = solutionRepository;
        this.githubClient = githubClient;
        this.executor = Executors.newCachedThreadPool(); // TODO choose best implementation + better init.
    }

    /**
     * Returns assigned solutionId and performs solution counting/status update in separate thread.
     */
    public String process(Solution solution) {

        String solutionId = solutionRepository.save(solution.setStatus(SolutionStatus.IN_PROGRESS));

        executor.execute(() -> {
            if (isBranch(solution.getRepo(), solution.getCommit())) {
                String commit = githubClient.getCommit(solution.getRepo(), solution.getCommit());
                solution.setCommit(commit);
            }

            long score = count(solution);
            // TODO try catch to set correct status. set timeout!!!
            Solution toUpdate = solution.setId(solutionId).setScore(score).setStatus(SolutionStatus.ACCEPTED);
            solutionRepository.update(toUpdate);
        });

        return solutionId;
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
