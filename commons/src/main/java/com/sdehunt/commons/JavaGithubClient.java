package com.sdehunt.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdehunt.commons.params.HardCachedParameterService;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.commons.params.SsmParameterService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Client for Github repositories
 */
public class JavaGithubClient implements GithubClient {

    private final static String RAW_DOMAIN = "https://raw.githubusercontent.com";
    private final static String API_DOMAIN = "https://api.github.com";
    private final static String REPOS = "repos";
    private final static String COMMITS = "commits";
    private static final String ACCESS_TOKEN = "GITHUB_ACCESS_TOKEN"; //  TODO autowired?
    private static final ParameterService params = new HardCachedParameterService(new SsmParameterService());
    private final HttpClient client = HttpClient.newHttpClient(); // TODO autowired?
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void download(String repo, String commit, String file) {
        try {
            URI uri = new URI(RAW_DOMAIN + "/" + repo + "/" + commit + "/" + file);
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", "token " + params.get(ACCESS_TOKEN))
                    .uri(uri)
                    .build();

            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(createFile(file)));

            if (response.statusCode() != 200) {
                throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*
        For https://github.com/AnatoliiStepaniuk/sdehunt/tree/test-branch should return 0f3d6f47337ca63bcd87a3bd20a4d1e90bced703
        For https://github.com/AnatoliiStepaniuk/sdehunt/tree/master or https://github.com/AnatoliiStepaniuk/sdehunt should return ad903026b3541fddf80f23b99ab48eae27a03a1f
     */

    @Override
    public String getCommit(String repo, String branch) {
        try {
            URI uri = new URI(API_DOMAIN + "/" + REPOS + "/" + repo + "/" + COMMITS + "/" + branch);
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", "token " + params.get(ACCESS_TOKEN)) // TODo caching
                    .uri(uri)
                    .build();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Status code " + response.statusCode() + " for URI " + uri);
            }
            return objectMapper.readValue(response.body(), SimpleCommit.class).getSha();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Path createFile(String file) throws IOException {
        if (Files.exists(Paths.get(FileUtils.fileName(file)))) {
            Files.delete(Paths.get(FileUtils.fileName(file)));
        }
        return Files.createFile(Paths.get(FileUtils.fileName(file)));
    }
}
