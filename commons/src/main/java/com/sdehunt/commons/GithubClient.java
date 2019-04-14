package com.sdehunt.commons;

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
public class GithubClient {

    private final static String DOMAIN = "https://raw.githubusercontent.com";
    private static final String ACCESS_TOKEN = "GITHUB_ACCESS_TOKEN"; // TODO autowired?
    private static final ParameterService params = new HardCachedParameterService(new SsmParameterService());
    private final HttpClient client = HttpClient.newHttpClient(); // TODO autowired?

    /**
     * Downloads repo directory of repository on specified commit
     *
     * @param repo   Repository to download directory from
     * @param commit Commit to checkout before downloading
     * @param file   Path to file to download
     */
    public void download(String repo, String commit, String file) {
        try {
            URI uri = new URI(DOMAIN + "/" + repo + "/" + commit + "/" + file);
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

    private Path createFile(String file) throws IOException {
        if (Files.exists(Paths.get(FileUtils.fileName(file)))) {
            Files.delete(Paths.get(FileUtils.fileName(file)));
        }
        return Files.createFile(Paths.get(FileUtils.fileName(file)));
    }
}
