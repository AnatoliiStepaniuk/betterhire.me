package com.sdehunt.score;

import com.sdehunt.commons.file.FileDownloader;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.util.FileUtils;

import java.nio.file.Paths;
import java.util.List;

public class GeneralFilesDownloader {

    private final GithubClient githubClient;

    private final static String S3_PREFIX = "https://betterhire-tasks.s3.eu-central-1.amazonaws.com/";
    private final FileDownloader fileDownloader;

    public GeneralFilesDownloader(GithubClient githubClient, FileDownloader fileDownloader) {
        this.githubClient = githubClient;
        this.fileDownloader = fileDownloader;
    }

    public void downloadInputFiles(List<String> inputFiles) {
        for (String f : inputFiles) { // TODO it once CACHE IT SOMEHOW
            fileDownloader.download(S3_PREFIX + f, Paths.get(FileUtils.fileName(f)));// TODO name files so that it does not clunch with files for other tasks
        }
    }

    public void downloadSolutionFiles(String userId, String repo, String commit, List<String> solutionFiles) throws CommitOrFileNotFoundException {
        for (String f : solutionFiles) {
            githubClient.download(userId, repo, commit, f);
        }
    }
}
