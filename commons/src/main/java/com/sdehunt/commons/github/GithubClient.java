package com.sdehunt.commons.github;

import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;

import java.util.Collection;

public interface GithubClient {

    /**
     * Downloads repo directory of repository on specified commit
     *
     * @param repo   Repository to download directory from
     * @param commit Commit to checkout before downloading
     * @param file   Path to file to download
     */
    void download(String repo, String commit, String file) throws CommitOrFileNotFoundException;

    /**
     * Returns hash of the last commit for specified repository and branch.
     */
    String getCommit(String repo, String branch);

    /**
     * Returns collection of repository branches
     */
    Collection<String> getBranches(String repo) throws RepositoryNotFoundException;

    /**
     * Returns true if input string matches one of the repo branches
     */
    default boolean isBranch(String repo, String input) throws RepositoryNotFoundException {
        return getBranches(repo).contains(input);
    }

    /**
     * Default method for master branch
     */
    default String getCommit(String repo) {
        return getCommit(repo, "master");
    }

}
