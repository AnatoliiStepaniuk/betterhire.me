package com.sdehunt.commons.github;

import com.sdehunt.commons.cache.Cache;
import com.sdehunt.commons.cache.CacheImpl;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.commons.github.exceptions.GithubTimeoutException;
import com.sdehunt.commons.github.exceptions.RepositoryNotFoundException;
import com.sdehunt.commons.github.model.FileInfoDTO;
import com.sdehunt.commons.github.model.IssueDTO;
import com.sdehunt.commons.model.Language;

import java.util.Collection;
import java.util.Optional;

/**
 * Caches some of the responses of underlying client
 */
public class CachingGithubClient implements GithubClient {

    private static final String SEP = " ";
    private final GithubClient inner;
    private Cache<String, String> cache;

    public CachingGithubClient(GithubClient inner) {
        this.inner = inner;
        this.cache = new CacheImpl<>("github", String.class, String.class, 200, 60 * 60);
    }

    @Override
    public void download(String userId, String repo, String commit, String file) throws CommitOrFileNotFoundException {
        inner.download(userId, repo, commit, file);
    }

    @Override
    public String getCommit(String userId, String repo, String branch) {
        return inner.getCommit(userId, repo, branch);
    }

    @Override
    public boolean commitPresent(String userId, String repo, String commit) {
        return inner.commitPresent(userId, repo, commit);
    }

    @Override
    public Collection<String> getBranches(String userId, String repo) throws RepositoryNotFoundException, GithubTimeoutException {
        return inner.getBranches(userId, repo);
    }

    @Override
    public void copyRepo(String template, String owner, String repoName, String description, boolean isPrivate) {
        inner.copyRepo(template, owner, repoName, description, isPrivate);
    }

    @Override
    public String invite(String repo, String githubLogin) {
        return cache.computeIfAbsent(
                repo + SEP + githubLogin,
                s -> inner.invite(s.split(SEP)[0], s.split(SEP)[1])
        );
    }

    @Override
    public void createWebhook(String repo, String hookUrl, String secret) {
        inner.createWebhook(repo, hookUrl, secret);
    }

    @Override
    public Language getRepoLanguage(String repo) {
        return inner.getRepoLanguage(repo);
    }

    @Override
    public void openIssue(String repo, String title, String body, String assignee) {
        inner.openIssue(repo, title, body, assignee);
    }

    @Override
    public Collection<IssueDTO> getRepoIssues(String repo, boolean allStates) {
        return inner.getRepoIssues(repo, allStates);
    }

    @Override
    public void createRepo(String name, boolean isTemplate) {
        inner.createRepo(name, isTemplate);
    }

    @Override
    public void createFile(String repo, String filePath, String b64Content, String branch) {
        inner.createFile(repo, filePath, b64Content, branch);
    }

    @Override
    public void updateFile(String repo, String filePath, String b64Content, String sha, String branch) {
        inner.updateFile(repo, filePath, b64Content, sha, branch);
    }

    @Override
    public Optional<FileInfoDTO> getFileInfo(String repo, String filePath, String ref) {
        return inner.getFileInfo(repo, filePath, ref);
    }
}
