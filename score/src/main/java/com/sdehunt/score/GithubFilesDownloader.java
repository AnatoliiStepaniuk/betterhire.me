package com.sdehunt.score;

import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;

import java.util.List;

public class GithubFilesDownloader implements FilesDownloader {

    private final GithubClient githubClient;

    public GithubFilesDownloader(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    @Override
    public void downloadInputFiles(String repo, String branch, List<String> inputFiles) throws CommitOrFileNotFoundException {
        for (String f : inputFiles) { // TODO it once CACHE IT SOMEHOW
            githubClient.download(repo, branch, f);// TODO name files so that it does not clunch with files for other tasks
        }
    }

    @Override
    public void downloadSolutionFiles(String userId, String repo, String commit, List<String> solutionFiles) throws CommitOrFileNotFoundException {
        for (String f : solutionFiles) {
            githubClient.download(userId, repo, commit, f);
        }
    }
}
