package com.sdehunt.commons.github;

import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.GithubTimeoutException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;

import java.util.Collection;

public interface GithubClient {

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
     */
    void copyRepo(String template, String repoName);

    /**
     * Invites user to be a collaborator for the repo and returns invitation link.
     */
    String invite(String repo, String githubLogin);

    /**
     * Registers a push webhook for specified url
     *
     * @param repoName
     */
    void createWebhook(String repoName, String url, String secret);

}
