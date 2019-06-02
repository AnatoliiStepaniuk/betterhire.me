package com.sdehunt.commons.github;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdehunt.commons.FileUtils;
import com.sdehunt.commons.SimpleCommit;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.params.ParameterService;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Client for Github repositories
 */
public class JavaGithubClient implements GithubClient {

    private final static String RAW_DOMAIN = "https://raw.githubusercontent.com";
    private final static String API_DOMAIN = "https://api.github.com";
    private final static String GIT = "git";
    private final static String REPOS = "repos";
    private final static String COMMITS = "commits";
    private final static String BRANCHES = "branches";
    private static final String ACCESS_TOKEN = "GITHUB_ACCESS_TOKEN";
    private final ParameterService params;
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final Logger logger;

    public JavaGithubClient(ParameterService params) {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.params = params;
        this.logger = LoggerFactory.getLogger(JavaGithubClient.class);
    }

    @SneakyThrows({IOException.class, URISyntaxException.class, InterruptedException.class})
    public void download(String repo, String commit, String file) throws CommitOrFileNotFoundException {
        URI uri = new URI(RAW_DOMAIN + "/" + repo + "/" + commit + "/" + file);

        logger.debug(String.format("Downloading file %s of repo %s for commit %s", file, repo, commit));
        HttpResponse<Path> response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofFile(createFile(file)));

        if (response.statusCode() == 503) { // Rarely reproduced issue of returning 503 status code
            logger.warn(String.format("Second attempt to download file %s of repo %s for commit %s", file, repo, commit));
            response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofFile(createFile(file)));
        }

        if (response.statusCode() == 404) {
            throw new CommitOrFileNotFoundException();
        }

        if (response.statusCode() != 200) {
            throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
        }
        logger.debug(String.format("Downloaded file %s of repo %s for commit %s", file, repo, commit));
    }

    @Override
    @SneakyThrows({IOException.class, URISyntaxException.class, InterruptedException.class})
    public String getCommit(String repo, String branch) {
        URI uri = new URI(API_DOMAIN + "/" + REPOS + "/" + repo + "/" + COMMITS + "/" + branch);
        logger.debug(String.format("Fetching commits for repo %s and branch %s", repo, branch));
        HttpResponse<byte[]> response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofByteArray());
        logger.debug(String.format("Received response %d for commits request for repo %s and branch %s", response.statusCode(), repo, branch));

        if (response.statusCode() != 200) {
            throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
        }
        return objectMapper.readValue(response.body(), SimpleCommit.class).getSha();
    }

    @Override
    @SneakyThrows({IOException.class, URISyntaxException.class, InterruptedException.class})
    public boolean commitPresent(String repo, String commit) {
        URI uri = new URI(API_DOMAIN + "/" + REPOS + "/" + repo + "/" + GIT + "/" + COMMITS + "/" + commit);

        logger.debug(String.format("Checking commit %s for repo %s", commit, repo));
        HttpResponse<byte[]> response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofByteArray());
        logger.debug(String.format("Checked commit %s for repo %s", commit, repo));

        if (response.statusCode() == 200) {
            return true;
        } else if (response.statusCode() == 404) {
            return false;
        } else {
            throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
        }
    }

    @Override
    @SneakyThrows({IOException.class, URISyntaxException.class, InterruptedException.class})
    public Collection<String> getBranches(String repo) throws RepositoryNotFoundException {
        URI uri = new URI(API_DOMAIN + "/" + REPOS + "/" + repo + "/" + BRANCHES);

        logger.debug(String.format("Fetching branches for repo %s", repo)); // TODO timeouts happen here...
        HttpResponse<byte[]> response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofByteArray());
        logger.debug(String.format("Received response %d for branches request for repo %s", response.statusCode(), repo));
        if (response.statusCode() == 404) {
            throw new RepositoryNotFoundException(repo);
        }
        if (response.statusCode() != 200) {
            throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
        }
        return Arrays.stream(objectMapper.readValue(response.body(), BranchResponse[].class))
                .map(BranchResponse::getName)
                .collect(Collectors.toList());

    }

    private HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder()
                .header("Authorization", "token " + params.get(ACCESS_TOKEN))
                .uri(uri)
                .build();
    }

    private Path createFile(String file) throws IOException {
        if (Files.exists(Paths.get(FileUtils.fileName(file)))) {
            Files.delete(Paths.get(FileUtils.fileName(file)));
        }
        return Files.createFile(Paths.get(FileUtils.fileName(file)));
    }

    @Data
    private static class BranchResponse {
        private String name;
        private CommitResponse commit;
    }

    @Data
    private static class CommitResponse {
        private String sha;
        private String url;
    }
}
