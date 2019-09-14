package com.sdehunt.service;

import com.sdehunt.commons.exception.InvalidSolutionException;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.GithubTimeoutException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.Task;
import com.sdehunt.commons.model.TaskType;
import com.sdehunt.commons.model.User;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.exception.CommitNotFoundException;
import com.sdehunt.exception.SolutionIsPresentException;
import com.sdehunt.exception.TooManyRequestsException;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.TaskRepository;
import com.sdehunt.repository.UserRepository;
import com.sdehunt.score.GeneralScoreCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.*;

import static com.sdehunt.commons.model.SolutionStatus.*;

public class SolutionService {

    private GeneralScoreCounter scoreCounter;
    private TaskRepository taskRepository;
    private SolutionRepository solutionRepository;
    private UserRepository userRepository;
    private GithubClient githubClient;
    private final Logger logger;
    private ExecutorService executor;
    private ParameterService params;
    private BestSolutionService bestSolutionService;
    private ProfileNotificationService profileNotificationService;

    public SolutionService(
            GeneralScoreCounter scoreCounter,
            TaskRepository taskRepository,
            SolutionRepository solutionRepository,
            UserRepository userRepository,
            GithubClient githubClient,
            ParameterService params,
            BestSolutionService bestSolutionService,
            ProfileNotificationService profileNotificationService
    ) {
        this.taskRepository = taskRepository;
        this.scoreCounter = scoreCounter;
        this.solutionRepository = solutionRepository;
        this.userRepository = userRepository;
        this.githubClient = githubClient;
        this.executor = Executors.newCachedThreadPool();
        this.params = params;
        this.logger = LoggerFactory.getLogger(SolutionService.class);
        this.bestSolutionService = bestSolutionService;
        this.profileNotificationService = profileNotificationService;
    }

    /**
     * Returns assigned solutionId and performs solution counting/status update in separate thread.
     */
    public String process(Solution solution) {
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
        Task task = taskRepository.get(solution.getTaskId()).orElseThrow();

        String solutionId = solutionRepository.save(solution.setStatus(task.getType() == TaskType.AUTO ? IN_PROGRESS : WAITING_FOR_REVIEW));
        solution.setId(solutionId);

        User user = userRepository.get(solution.getUserId()).orElseThrow();
        updateUser(user, solution.getRepo());
        profileNotificationService.notifyIfNotFilled(user, solution.getRepo());

        if (task.getType() == TaskType.AUTO) {
            countScoreBackground(solution);
        } else {
            // For manual task we count each user submitted the solution (not only successful ones.
            int solved = solutionRepository.getNumberUsersSolvedTask(task.getId());
            task.setUsers(solved).setLastSubmit(Instant.now());
            taskRepository.update(task);
        }

        return solutionId;
    }

    private void updateUser(User user, String repo) {
        user.setLastSubmit(Instant.now());
        user.getLanguages().add(githubClient.getRepoLanguage(repo));
        userRepository.update(user);
    }

    private void countScoreBackground(Solution solution) {
        executor.execute(() -> {
            Future<Long> future = executor.submit(getCountScoreTask(solution));
            try {
                future.get(Long.valueOf(params.get("SOLUTION_COUNTER_TIMEOUT_SECONDS")), TimeUnit.SECONDS);
            } catch (InterruptedException | TimeoutException e) {
                future.cancel(true);
                logger.error("Solution " + solution.getId() + " finished with timeout exception", e);
                String cause = "Solution verification took too long to verify and was aborted before finished. Are your files too big?";
                solutionRepository.update(solution.setStatus(TIMEOUT).setCause(cause));
            } catch (ExecutionException e) {
                if (e.getCause() instanceof CommitOrFileNotFoundException) {
                    String cause = "Commit or solution file not found. Please check that commit/branch you specified exist. Do your files have correct naming and directory?";
                    solutionRepository.update(solution.setStatus(INVALID_FILES).setCause(cause));
                } else if (e.getCause() instanceof com.sdehunt.exception.RepositoryNotFoundException) {
                    String cause = "Repository not found. Please check that repository you've specified is the one you own.";
                    solutionRepository.update(solution.setStatus(INVALID_FILES).setCause(cause));
                } else if (e.getCause() instanceof InvalidSolutionException) {
                    String cause = ((InvalidSolutionException) e.getCause()).getDescription();
                    solutionRepository.update(solution.setStatus(INVALID_SOLUTION).setCause(cause));
                } else {
                    logger.error("Solution " + solution.getId() + " finished with error: ", e);
                    solutionRepository.update(solution.setStatus(ERROR).setCause(e.getMessage()));
                }
            }
        });
    }

    private Callable<Long> getCountScoreTask(final Solution solution) {
        return () -> {
            long score = count(solution);
            Solution toUpdate = solution.setScore(score).setStatus(ACCEPTED);
            solutionRepository.update(toUpdate);
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
