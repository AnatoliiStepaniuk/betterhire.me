package com.sdehunt.score;

import com.sdehunt.commons.file.FileDownloader;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.util.FileUtils;

import java.util.Collections;
import java.util.List;

public class GeneralFilesDownloader {

    private final GithubClient githubClient;

    private final static String S3_PREFIX = "https://betterhire-tasks.s3.eu-central-1.amazonaws.com/";
    private final FileDownloader fileDownloader;

    public GeneralFilesDownloader(GithubClient githubClient, FileDownloader fileDownloader) {
        this.githubClient = githubClient;
        this.fileDownloader = fileDownloader;
    }

    public void downloadInputFile(String inputFile) {
        downloadInputFiles(Collections.singletonList(inputFile));
    }
    public void downloadInputFiles(List<String> inputFiles) {
        for (String f : inputFiles) { // TODO it once CACHE IT SOMEHOW
            fileDownloader.download(S3_PREFIX + f, FileUtils.fileName(f));// TODO name files so that it does not clunch with files for other tasks
        }
    }

    public void downloadSolutionFile(String userId, String repo, String commit, String solutionFile) throws CommitOrFileNotFoundException {
        downloadSolutionFiles(userId, repo, commit, Collections.singletonList(solutionFile));
    }

    public void downloadSolutionFiles(String userId, String repo, String commit, List<String> solutionFiles) throws CommitOrFileNotFoundException {
        for (String f : solutionFiles) {
            githubClient.download(userId, repo, commit, f);
        }
    }
}
