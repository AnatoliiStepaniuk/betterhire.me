package com.sdehunt.service;

import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.model.FileInfoDTO;

import java.util.Base64;
import java.util.Optional;

public class ReadmeService {

    private final static String README = "README.md";
    private final static String README_FOOTER = " \n" +
            "\n" +
            "### To submit your solution just push your code to this repo \uD83D\uDE0E\n" +
            "\n" +
            "Happy coding! \uD83D\uDE09";
    private final GithubClient githubClient;
    private final String githubLogin;


    public ReadmeService(GithubClient githubClient, String githubLogin) {
        this.githubClient = githubClient;
        this.githubLogin = githubLogin;
    }

    public void create(String taskId, String description) {
        githubClient.createRepo(taskId, true);
        String b64Content = Base64.getEncoder().encodeToString((description + README_FOOTER).getBytes());
        githubClient.createFile(githubLogin + "/" + taskId, README, b64Content);
    }

    public void update(String taskId, String description) {
        Optional<FileInfoDTO> readme = githubClient.getFileInfo(githubLogin + "/" + taskId, README);
        String b64Content = Base64.getEncoder().encodeToString((description + README_FOOTER).getBytes());
        if (readme.isPresent()) {
            githubClient.updateFile(githubLogin + "/" + taskId, README, b64Content, readme.get().getSha());
        } else {
            githubClient.createFile(githubLogin + "/" + taskId, README, b64Content);
        }
    }
}
