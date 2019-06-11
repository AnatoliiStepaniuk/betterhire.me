package com.sdehunt.commons.github;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sdehunt.commons.FileUtils;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.GithubTimeoutException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.model.SimpleCommit;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.commons.repo.AccessTokenRepository;
import com.sdehunt.commons.security.AccessToken;
import com.sdehunt.commons.security.OAuthProvider;
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
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Client for Github repositories
 */
public class UnirestGithubClient implements GithubClient {

    private final static String RAW_DOMAIN = "https://raw.githubusercontent.com";
    private final static String API_DOMAIN = "https://api.github.com";
    private final static String GIT = "git";
    private final static String REPOS = "repos";
    private final static String COMMITS = "commits";
    private final static String BRANCHES = "branches";
    private static final String ACCESS_TOKEN = "GITHUB_ACCESS_TOKEN";
    private final static int TIMEOUT_ATTEMPTS = 3;
    private final static int TIMEOUT_MILLIS = 3000;
    private final ParameterService params;
    private final HttpClient client;
    private final AccessTokenRepository accessTokens;
    private final ObjectMapper objectMapper;
    private final Logger logger;
    private final ExecutorService executor;

    public UnirestGithubClient(ParameterService params, AccessTokenRepository accessTokens) {
        this.client = HttpClient.newHttpClient();
        this.accessTokens = accessTokens;
        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.params = params;
        this.logger = LoggerFactory.getLogger(UnirestGithubClient.class);
        this.executor = Executors.newSingleThreadExecutor();

    }

    @Override
    @SneakyThrows({IOException.class, URISyntaxException.class, InterruptedException.class})
    public void download(String userId, String repo, String commit, String file) throws CommitOrFileNotFoundException {
        URI uri = new URI(RAW_DOMAIN + "/" + repo + "/" + commit + "/" + file);

        logger.debug(String.format("Downloading file %s of repo %s for commit %s", file, repo, commit));
        HttpResponse<Path> response = client.send(buildRequest(uri, userId), HttpResponse.BodyHandlers.ofFile(createFile(file)));

        if (response.statusCode() == 503) { // Rarely reproduced issue of returning 503 status code
            logger.warn(String.format("Second attempt to download file %s of repo %s for commit %s", file, repo, commit));
            response = client.send(buildRequest(uri, userId), HttpResponse.BodyHandlers.ofFile(createFile(file)));
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
    @SneakyThrows({IOException.class, UnirestException.class})
    public String getCommit(String userId, String repo, String branch) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + COMMITS + "/" + branch;
        logger.debug(String.format("Fetching commits for repo %s and branch %s", repo, branch));
        com.mashape.unirest.http.HttpResponse<String> response = Unirest.get(url).header("Authorization", "token " + getToken(userId)).asString();
        logger.debug(String.format("Received response %d for commits request for repo %s and branch %s", response.getStatus(), repo, branch));
        if (response.getStatus() != 200) {
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
        return objectMapper.readValue(response.getBody(), SimpleCommit.class).getSha();
    }

    @Override
    @SneakyThrows({UnirestException.class})
    public boolean commitPresent(String userId, String repo, String commit) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + GIT + "/" + COMMITS + "/" + commit;

        logger.debug(String.format("Checking commit %s for repo %s", commit, repo));
        com.mashape.unirest.http.HttpResponse<String> response = Unirest.get(url).header("Authorization", "token " + getToken(userId)).asString();

        logger.debug(String.format("Checked commit %s for repo %s", commit, repo));

        if (response.getStatus() == 200) {
            return true;
        } else if (response.getStatus() == 404) {
            return false;
        } else {
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
    }

    @Override
    @SneakyThrows({InterruptedException.class})
    public Collection<String> getBranches(String userId, String repo) throws RepositoryNotFoundException, GithubTimeoutException {
        for (int i = 0; i < TIMEOUT_ATTEMPTS; i++) {
            try {
                return executor.submit(() -> getBranchesOnce(userId, repo))
                        .get(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            } catch (TimeoutException ignored) {
                logger.warn(String.format("Received timeout for branches request for repo %s", repo));
            } catch (ExecutionException e) {
                if (e.getCause() instanceof RepositoryNotFoundException) {
                    throw (RepositoryNotFoundException) e.getCause();
                }
                throw new RuntimeException(e.getCause());
            }
        }
        throw new GithubTimeoutException();
    }

    @SneakyThrows({IOException.class, UnirestException.class})
    private Collection<String> getBranchesOnce(String userId, String repo) throws RepositoryNotFoundException {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + BRANCHES;

        logger.debug(String.format("Fetching branches for repo %s", repo));
        com.mashape.unirest.http.HttpResponse<String> response = Unirest.get(url).header("Authorization", "token " + getToken(userId)).asString();

        logger.debug(String.format("Received response %d for branches request for repo %s", response.getStatus(), repo));
        if (response.getStatus() == 404) {
            throw new RepositoryNotFoundException(repo);
        }
        if (response.getStatus() != 200) {
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
        return Arrays.stream(objectMapper.readValue(response.getBody(), BranchResponse[].class))
                .map(BranchResponse::getName)
                .collect(Collectors.toList());

    }

    private HttpRequest buildRequest(URI uri, String userId) {// TODO remove
        String token = accessTokens.find(userId, OAuthProvider.GITHUB).map(AccessToken::getToken)
                .orElseGet(() -> params.get(ACCESS_TOKEN));
        logger.debug("Using access token: " + token);
        return HttpRequest.newBuilder()
                .header("Authorization", "token " + token)
                .uri(uri)
                .build();
    }

    private String getToken(String userId) {
        return accessTokens.find(userId, OAuthProvider.GITHUB).map(AccessToken::getToken)
                .orElseGet(() -> params.get(ACCESS_TOKEN));
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
