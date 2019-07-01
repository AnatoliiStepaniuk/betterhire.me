package com.sdehunt.service;

import com.sdehunt.commons.exception.InvalidSolutionException;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.GithubTimeoutException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.commons.model.User;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.commons.util.RepoUtils;
import com.sdehunt.exception.CommitNotFoundException;
import com.sdehunt.exception.SolutionIsPresentException;
import com.sdehunt.exception.TooManyRequestsException;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.UserRepository;
import com.sdehunt.score.GeneralScoreCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.*;

public class SolutionService {

    private GeneralScoreCounter scoreCounter;
    private SolutionRepository solutionRepository;
    private UserRepository userRepository;
    private GithubClient githubClient;
    private final Logger logger;
    private ExecutorService executor;
    private ParameterService params;

    private BestSolutionService bestSolutionService;

    public SolutionService(
            GeneralScoreCounter scoreCounter,
            SolutionRepository solutionRepository,
            UserRepository userRepository,
            GithubClient githubClient,
            ParameterService params,
            BestSolutionService bestSolutionService
    ) {
        this.scoreCounter = scoreCounter;
        this.solutionRepository = solutionRepository;
        this.userRepository = userRepository;
        this.githubClient = githubClient;
        this.executor = Executors.newCachedThreadPool();
        this.params = params;
        this.logger = LoggerFactory.getLogger(SolutionService.class);
        this.bestSolutionService = bestSolutionService;
    }

    /**
     * Returns assigned solutionId and performs solution counting/status update in separate thread.
     */
    public String process(Solution solution) {
        solution.setRepo(RepoUtils.trimRepo(solution.getRepo()));
        if (isBranch(solution.getUserId(), solution.getRepo(), solution.getCommit())) {
            String commit = githubClient.getCommit(solution.getUserId(), solution.getRepo(), solution.getCommit());
            solution.setCommit(commit);
        } else {
            if (!githubClient.commitPresent(solution.getUserId(), solution.getRepo(), solution.getCommit())) {
                throw new CommitNotFoundException();
            }
        }

        if (solutionRepository.isPresentForUser(solution)) {
            throw new SolutionIsPresentException();
        }

        String solutionId = solutionRepository.save(solution.setStatus(SolutionStatus.IN_PROGRESS));
        solution.setId(solutionId);

        executor.execute(() -> {
            Future<Long> future = executor.submit(getCountScoreTask(solution));
            try {
                future.get(Long.valueOf(params.get("SOLUTION_COUNTER_TIMEOUT_SECONDS")), TimeUnit.SECONDS);
            } catch (InterruptedException | TimeoutException e) {
                future.cancel(true);
                logger.error("Solution " + solutionId + " finished with timeout exception", e);
                solutionRepository.update(solution.setStatus(SolutionStatus.TIMEOUT));
            } catch (ExecutionException e) {
                if (e.getCause() instanceof CommitOrFileNotFoundException
                        || e.getCause() instanceof com.sdehunt.exception.RepositoryNotFoundException) {
                    solutionRepository.update(solution.setStatus(SolutionStatus.INVALID_FILES));
                } else if (e.getCause() instanceof InvalidSolutionException) {
                    solutionRepository.update(solution.setStatus(SolutionStatus.INVALID_SOLUTION));
                } else {
                    logger.error("Solution " + solutionId + " finished with error: ", e);
                    solutionRepository.update(solution.setStatus(SolutionStatus.ERROR));
                }
            }
        });

        return solutionId;
    }

    private Callable<Long> getCountScoreTask(final Solution solution) {
        return () -> {
            long score = count(solution);
            Solution toUpdate = solution.setScore(score).setStatus(SolutionStatus.ACCEPTED);
            solutionRepository.update(toUpdate);
            User user = userRepository.get(solution.getUserId()).orElseThrow().setLastSubmit(Instant.now());
            userRepository.update(user);
            bestSolutionService.updateIfNeeded(solution, score);
            return score;
        };
    }


    private boolean isBranch(String userId, String repo, String input) {
        try {
            return githubClient.isBranch(userId, repo, input);
        } catch (RepositoryNotFoundException e) {
            throw new com.sdehunt.exception.RepositoryNotFoundException(repo);
        } catch (GithubTimeoutException e) {
            throw new TooManyRequestsException();
        }
    }

    private long count(Solution solution) throws CommitOrFileNotFoundException {
        return scoreCounter.count(solution);
    }
}
