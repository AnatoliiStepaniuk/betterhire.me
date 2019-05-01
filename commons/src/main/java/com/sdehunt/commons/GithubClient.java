package com.sdehunt.commons;

public interface GithubClient {

    /**
     * Downloads repo directory of repository on specified commit
     *
     * @param repo   Repository to download directory from
     * @param commit Commit to checkout before downloading
     * @param file   Path to file to download
     */
    void download(String repo, String commit, String file);

    /**
     * Returns hash of the last commit for specified repository and branch.
     */
    String getCommit(String repo, String branch);

    /**
     * Default method for master branch
     */
    default String getCommit(String repo) {
        return getCommit(repo, "master");
    }

}
