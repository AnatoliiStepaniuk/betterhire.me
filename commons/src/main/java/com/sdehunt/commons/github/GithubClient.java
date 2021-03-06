package com.sdehunt.commons.github;

import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.GithubTimeoutException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.github.model.FileInfoDTO;
import com.sdehunt.commons.github.model.IssueDTO;
import com.sdehunt.commons.model.Language;

import java.util.Collection;
import java.util.Optional;

public interface GithubClient {

    String MASTER = "master";

    /**
     * Downloads repo directory of repository on specified commit
     *
     * @param userId Identifier of user in our system
     * @param repo   Repository to download directory from
     * @param commit Commit to checkout before downloading
     * @param file   Path to file to download
     */
    void download(String userId, String repo, String commit, String file) throws CommitOrFileNotFoundException;

    default void download(String repo, String commit, String file) throws CommitOrFileNotFoundException {
        download("", repo, commit, file);
    }

    /**
     * Returns hash of the last commit for specified repository and branch.
     */
    String getCommit(String userId, String repo, String branch);

    default String getCommit(String repo, String branch) {
        return getCommit("", repo, branch);
    }

    /**
     * Returns true if commit with specified hash is present
     */
    boolean commitPresent(String userId, String repo, String commit);

    default boolean commitPresent(String repo, String commit) {
        return commitPresent("", repo, commit);
    }

    /**
     * Returns collection of repository branches
     */
    Collection<String> getBranches(String userId, String repo) throws RepositoryNotFoundException, GithubTimeoutException;

    default Collection<String> getBranches(String repo) throws RepositoryNotFoundException, GithubTimeoutException {
        return getBranches("", repo);
    }

    /**
     * Returns true if input string matches one of the repo branches
     */
    default boolean isBranch(String userId, String repo, String input) throws RepositoryNotFoundException, GithubTimeoutException {
        return getBranches(userId, repo).contains(input);
    }

    /**
     * Creates a new repository from template repo.
     *
     * @param template    Repository to create copy from
     * @param owner       Owner of newly created repo
     * @param repoName    New repo name
     * @param description Description of new repo
     * @param isPrivate   If new repository should be private
     */
    void copyRepo(String template, String owner, String repoName, String description, boolean isPrivate);

    /**
     * Invites user to be a collaborator for the repo and returns invitation link.
     */
    String invite(String repo, String githubLogin);

    /**
     * Registers a push webhook for specified url
     *
     * @param repo Username + reponame
     */
    void createWebhook(String repo, String hookUrl, String secret);

    /**
     * Returns main language of repository
     *
     * @param repo repository to get language of
     * @return Main language of repository or OTHER if not present in our system.
     */
    Language getRepoLanguage(String repo);

    /**
     * Opens issue in specified repository.
     *
     * @param repo     repo to open issue in
     * @param title    title of the issue
     * @param body     body of the issue
     * @param assignee github user that will be assigned this issue
     */
    void openIssue(String repo, String title, String body, String assignee);

    /**
     * Returns all issues of the repository.
     *
     * @param repo      repository to return issues of
     * @param allStates if true issues in all states will be returned. Otherwise, only open ones.
     * @return list of repository issues
     */
    Collection<IssueDTO> getRepoIssues(String repo, boolean allStates);

    /**
     * Creates repository for authenticated user.
     */
    void createRepo(String name, boolean isTemplate);

    /**
     * Creates file in specified repository.
     */
    void createFile(String repo, String filePath, String b64Content, String branch);

    /**
     * Updates file in specified repository.
     */
    void updateFile(String repo, String filePath, String b64Content, String sha, String branch);

    /**
     * Gets file info, if present
     * WARNING! Only files less than 1Mb are supported
     */
    Optional<FileInfoDTO> getFileInfo(String repo, String filePath, String ref);

    default Optional<FileInfoDTO> getFileInfo(String repo, String filePath) {
        return getFileInfo(repo, filePath, MASTER);
    }

    default void createFile(String repo, String filePath, String b64Content) {
        createFile(repo, filePath, b64Content, MASTER);
    }

    default void updateFile(String repo, String filePath, String b64Content, String sha) {
        updateFile(repo, filePath, b64Content, sha, MASTER);
    }

}
