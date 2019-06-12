package com.sdehunt.score;

import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;

import java.util.List;

public interface FilesDownloader {

    void downloadInputFiles(String repo, String branch, List<String> inputFiles) throws CommitOrFileNotFoundException;

    void downloadSolutionFiles(String userId, String repo, String commit, List<String> solutionFiles) throws CommitOrFileNotFoundException;

}
