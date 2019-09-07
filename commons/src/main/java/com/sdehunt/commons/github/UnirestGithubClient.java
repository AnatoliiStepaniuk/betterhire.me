package com.sdehunt.commons.github;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.GithubTimeoutException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.github.model.*;
import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.SimpleCommit;
import com.sdehunt.commons.repo.AccessTokenRepository;
import com.sdehunt.commons.util.FileUtils;
import kong.unirest.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Client for Github repositories
 */
public class UnirestGithubClient implements GithubClient {

    private final static String RAW_DOMAIN = "https://raw.githubusercontent.com";
    private final static String API_DOMAIN = "https://api.github.com";
    private final static String WEB_DOMAIN = "https://github.com";
    private final static String GIT = "git";
    private final static String REPOS = "repos";
    private final static String COMMITS = "commits";
    private final static String BRANCHES = "branches";
    private final static String GENERATE = "generate";
    private final static String COLLABORATORS = "collaborators";
    private final static String HOOKS = "hooks";
    private final static String ISSUES = "issues";
    private final static String LANGUAGES = "languages";
    private final String systemAccessToken;
    private final static int TIMEOUT_ATTEMPTS = 3;
    private final static int TIMEOUT_MILLIS = 3000;
    private final AccessTokenRepository accessTokens;
    private final ObjectMapper objectMapper;
    private final Logger logger;
    private final ExecutorService executor;

    public UnirestGithubClient(String systemAccessToken, AccessTokenRepository accessTokens) {
        this.accessTokens = accessTokens;
        this.systemAccessToken = systemAccessToken;
        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper));
        this.logger = LoggerFactory.getLogger(UnirestGithubClient.class);
        this.executor = Executors.newSingleThreadExecutor();

    }

    @Override
    @SneakyThrows({UnirestException.class, IOException.class})
    public void download(String userId, String repo, String commit, String file) throws CommitOrFileNotFoundException {
        String path = RAW_DOMAIN + "/" + repo + "/" + commit + "/" + file;

        logger.debug(format("Downloading file %s of repo %s for commit %s", file, repo, commit));
        if (Files.exists(Path.of(FileUtils.fileName(file)))) {
            Files.delete(Path.of(FileUtils.fileName(file)));
        }
        HttpResponse<File> response = Unirest.get(path)
                .header("Authorization", "token " + systemAccessToken)
                .asFile(FileUtils.fileName(file));

        if (response.getStatus() == 404) {
            logger.debug(format("File %s of repo %s for commit %s was not found", file, repo, commit));
            throw new CommitOrFileNotFoundException();
        }

        if (response.getStatus() != 200) {
            logger.warn("Status code " + response.getStatus() + " for URI " + path);
            throw new RuntimeException("Status code " + response.getStatus() + " for URI " + path);
        }

        logger.debug(format("Downloaded file %s of repo %s for commit %s", file, repo, commit));
    }

    @Override
    @SneakyThrows({IOException.class, UnirestException.class})
    public String getCommit(String userId, String repo, String branch) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + COMMITS + "/" + branch;
        logger.debug(format("Fetching commits for repo %s and branch %s", repo, branch));
        HttpResponse<String> response = Unirest.get(url).header("Authorization", "token " + systemAccessToken).asString();
        logger.debug(format("Received response %d for commits request for repo %s and branch %s", response.getStatus(), repo, branch));
        if (response.getStatus() != 200) {
            logger.warn("Status code " + response.getStatus() + " for URL " + url);
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
        return objectMapper.readValue(response.getBody(), SimpleCommit.class).getSha();
    }

    @Override
    @SneakyThrows({UnirestException.class})
    public boolean commitPresent(String userId, String repo, String commit) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + GIT + "/" + COMMITS + "/" + commit;

        logger.debug(format("Checking commit %s for repo %s", commit, repo));
        HttpResponse<String> response = Unirest.get(url).header("Authorization", "token " + systemAccessToken).asString();

        logger.debug(format("Checked commit %s for repo %s", commit, repo));

        if (response.getStatus() == 200) {
            return true;
        } else if (response.getStatus() == 404) {
            return false;
        } else {
            logger.warn("Status code " + response.getStatus() + " for URL " + url);
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
    }

    @SneakyThrows({InterruptedException.class})
    public Collection<String> getBranches(String userId, String repo) throws RepositoryNotFoundException, GithubTimeoutException {
        for (int i = 0; i < TIMEOUT_ATTEMPTS; i++) {
            try {
                return executor.submit(() -> getBranchesOnce(userId, repo))
                        .get(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            } catch (TimeoutException ignored) {
                logger.warn(format("Received timeout for branches request for repo %s", repo));
            } catch (ExecutionException e) {
                if (e.getCause() instanceof RepositoryNotFoundException) {
                    throw (RepositoryNotFoundException) e.getCause();
                }
                throw new RuntimeException(e.getCause());
            }
        }
        throw new GithubTimeoutException();
    }

    @Override
    public void copyRepo(String template, String owner, String repoName, String description, boolean isPrivate) {
        String url = API_DOMAIN + "/" + REPOS + "/" + template + "/" + GENERATE;
        logger.debug("Creating repo name {} for owner {} from template {} with description {}", repoName, owner, template, description);
        CopyRepoDTO body = new CopyRepoDTO()
                .setOwner(owner)
                .setName(repoName)
                .setDescription(description)
                .setPrivate(isPrivate);

        HttpResponse response = Unirest.post(url)
                .header("Authorization", "token " + systemAccessToken)
                .header("Accept", "application/vnd.github.baptiste-preview+json")
                .body(body)
                .asEmpty();

        if (response.getStatus() != 201) {
            logger.warn("Status code " + response.getStatus() + " for URL " + url + " body: " + response.getBody());
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
    }

    @Override
    @SneakyThrows({UnirestException.class})
    public String invite(String repo, String githubLogin) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + COLLABORATORS + "/" + githubLogin;
        HttpResponse<InvitationResponseDTO> response = Unirest.put(url)
                .header("Authorization", "token " + systemAccessToken)
                .asObject(InvitationResponseDTO.class);

        if (response.getStatus() == 201) {
            return response.getBody().getRepository().getHtmlUrl();
        }
        if (response.getStatus() == 204) {
            return WEB_DOMAIN + "/" + repo;
        }

        throw new RuntimeException(format("Unexpected status code (%d) for invitation request of user %s to repo %s", response.getStatus(), githubLogin, repo));
    }

    @Override
    @SneakyThrows({UnirestException.class})
    public void createWebhook(String repo, String hookUrl, String secret) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + HOOKS;
        CreateHookDTO body = new CreateHookDTO()
                .setConfig(
                        new CreateHookDTO.Config()
                                .setUrl(hookUrl)
                                .setContentType("json")
                                .setInsecureSSL("0")
                )
                .setEvents(Collections.singletonList("push"));
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Authorization", "token " + systemAccessToken)
                .body(body)
                .asJson();
        if (response.getStatus() != 201) {
            logger.warn("Status code " + response.getStatus() + " for URL " + url);
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
    }

    @Override
    @SneakyThrows({IOException.class})
    public Language getRepoLanguage(String repo) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + LANGUAGES;
        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", "token " + systemAccessToken)
                .asString();
        if (response.getStatus() != 200) {
            logger.warn("Status code " + response.getStatus() + " for URL " + url);
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }

        Map<String, Long> map = objectMapper.readValue(response.getBody(), Map.class);
        return map.entrySet().stream()
                .reduce((e1, e2) -> e1.getValue() > e2.getValue() ? e1 : e2)
                .map(Map.Entry::getKey)
                .map(GithubLanguageConverter::convert)
                .orElse(Language.OTHER);
    }

    @Override
    public void openIssue(String repo, String title, String body, String assignee) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + ISSUES;
        CreateIssueDTO dto = new CreateIssueDTO()
                .setTitle(title)
                .setBody(body)
                .setAssignees(Collections.singleton(assignee));
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Authorization", "token " + systemAccessToken)
                .body(dto)
                .asJson();
        if (response.getStatus() != 201) {
            logger.warn("Status code " + response.getStatus() + " for URL " + url);
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
    }

    @Override
    @SneakyThrows({IOException.class})
    public Collection<IssueDTO> getRepoIssues(String repo, boolean allStates) {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + ISSUES; // TODO verify that closed issue is also listed
        url += allStates ? "?state=all" : "";
        HttpResponse<String> response = Unirest.get(url)
                .header("Authorization", "token " + systemAccessToken)
                .asString();
        if (response.getStatus() != 200) {
            logger.warn("Status code " + response.getStatus() + " for URL " + url);
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }

        return Arrays.asList(objectMapper.readValue(response.getBody(), IssueDTO[].class));
    }

    @SneakyThrows({IOException.class, UnirestException.class})
    private Collection<String> getBranchesOnce(String userId, String repo) throws RepositoryNotFoundException {
        String url = API_DOMAIN + "/" + REPOS + "/" + repo + "/" + BRANCHES;

        logger.debug(format("Fetching branches for repo %s", repo));
        HttpResponse<String> response = Unirest.get(url).header("Authorization", "token " + systemAccessToken).asString();

        logger.debug(format("Received response %d for branches request for repo %s", response.getStatus(), repo));
        if (response.getStatus() == 404) {
            throw new RepositoryNotFoundException(repo);
        }
        if (response.getStatus() != 200) {
            logger.warn("Status code " + response.getStatus() + " for URL " + url);
            throw new RuntimeException("Status code " + response.getStatus() + " for URL " + url);
        }
        return Arrays.stream(objectMapper.readValue(response.getBody(), BranchResponse[].class))
                .map(BranchResponse::getName)
                .collect(Collectors.toList());

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
