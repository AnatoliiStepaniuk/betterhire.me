package com.sdehunt.commons;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdehunt.commons.params.HardCachedParameterService;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.commons.params.SsmParameterService;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URI;
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
    private final static String REPOS = "repos";
    private final static String COMMITS = "commits";
    private final static String BRANCHES = "branches";
    private static final String ACCESS_TOKEN = "GITHUB_ACCESS_TOKEN";
    private final ParameterService params;
    private final HttpClient client;
    private final ObjectMapper objectMapper;


    public JavaGithubClient() {
        this.client = HttpClient.newHttpClient(); // TODO autowired?
        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.params = new HardCachedParameterService(new SsmParameterService());
    }

    @SneakyThrows
    public void download(String repo, String commit, String file) {
        URI uri = new URI(RAW_DOMAIN + "/" + repo + "/" + commit + "/" + file);

        // TODO replace with logger
        System.out.println(String.format("Downloading file %s of repo %s for commit %s", file, repo, commit));
        HttpResponse<Path> response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofFile(createFile(file)));
        System.out.println(String.format("Downloaded file %s of repo %s for commit %s", file, repo, commit));

        if (response.statusCode() == 503) { // Rarely reproduced issue of returning 503 status code
            System.out.println(String.format("Second attempt to download file %s of repo %s for commit %s", file, repo, commit));
            response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofFile(createFile(file)));
            System.out.println(String.format("Download file %s of repo %s for commit %s from second attempt", file, repo, commit));
        }

        if (response.statusCode() != 200) {
            throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
        }
    }

    @Override
    public String getCommit(String repo, String branch) {
        try {
            URI uri = new URI(API_DOMAIN + "/" + REPOS + "/" + repo + "/" + COMMITS + "/" + branch);
            HttpResponse<byte[]> response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
            }
            return objectMapper.readValue(response.body(), SimpleCommit.class).getSha();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<String> getBranches(String repo) {
        try {
            URI uri = new URI(API_DOMAIN + "/" + REPOS + "/" + repo + "/" + BRANCHES);

            HttpResponse<byte[]> response = client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
            }
            return Arrays.stream(objectMapper.readValue(response.body(), BranchResponse[].class))
                    .map(BranchResponse::getName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
