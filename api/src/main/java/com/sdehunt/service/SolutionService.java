package com.sdehunt.service;

import com.sdehunt.commons.exception.InvalidSolutionException;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.JavaGithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.score.GeneralScoreCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class SolutionService {

    private GeneralScoreCounter scoreCounter;

    private SolutionRepository solutionRepository;

    private GithubClient githubClient;

    private final Logger logger;
    private ExecutorService executor;
    private ParameterService params;

    public SolutionService(
            GeneralScoreCounter scoreCounter,
            SolutionRepository solutionRepository,
            GithubClient githubClient,
            ParameterService params
    ) {
        this.scoreCounter = scoreCounter;
        this.solutionRepository = solutionRepository;
        this.githubClient = githubClient;
        this.executor = Executors.newCachedThreadPool();
        this.params = params;
        this.logger = LoggerFactory.getLogger(JavaGithubClient.class);
    }

    /**
     * Returns assigned solutionId and performs solution counting/status update in separate thread.
     */
    public String process(Solution solution) {

        String solutionId = solutionRepository.save(solution.setStatus(SolutionStatus.IN_PROGRESS));
        solution.setId(solutionId);

        executor.execute(() -> {
            Future<Long> future = executor.submit(getCountScoreTask(solution));
            try {
                future.get(Long.valueOf(params.get("SOLUTION_COUNTER_TIMEOUT_SECONDS")), TimeUnit.SECONDS);
            } catch (InterruptedException | TimeoutException e) {
                future.cancel(true);
                logger.error("Solution " + solutionId + " finished with exception", e);
                solutionRepository.update(solution.setStatus(SolutionStatus.TIMEOUT));
            } catch (ExecutionException e) {
                if (e.getCause() instanceof CommitOrFileNotFoundException
                        || e.getCause() instanceof com.sdehunt.exception.RepositoryNotFoundException) {
                    solutionRepository.update(solution.setStatus(SolutionStatus.INVALID_FILES));
                } else if (e.getCause() instanceof InvalidSolutionException) {
                    solutionRepository.update(solution.setStatus(SolutionStatus.INVALID_SOLUTION)); // TODO add test
                } else {
                    solutionRepository.update(solution.setStatus(SolutionStatus.ERROR));
                }
            }
        });

        return solutionId;
    }

    private Callable<Long> getCountScoreTask(final Solution solution) {
        return () -> {
            if (isBranch(solution.getRepo(), solution.getCommit())) {
                String commit = githubClient.getCommit(solution.getRepo(), solution.getCommit());
                solution.setCommit(commit);
            }

            long score = count(solution);
            Solution toUpdate = solution.setScore(score).setStatus(SolutionStatus.ACCEPTED);
            solutionRepository.update(toUpdate);
            return score;
        };
    }


    private boolean isBranch(String repo, String input) {
        try {
            return githubClient.isBranch(repo, input);
        } catch (RepositoryNotFoundException e) {
            throw new com.sdehunt.exception.RepositoryNotFoundException(repo);
        }
    }

    private long count(Solution solution) throws CommitOrFileNotFoundException {
        return scoreCounter.count(solution);
    }
}
