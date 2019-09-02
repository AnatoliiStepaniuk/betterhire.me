package com.sdehunt.service;

import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.model.User;

/**
 * Notifies user if his profile is not filled completely.
 */
public class ProfileNotificationService {

    private final GithubClient githubClient;

    public ProfileNotificationService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public void notifyIfNotFilled(User user, String repo) {
        if (!isFilled(user) && !issueExists(repo) && isSystemUser(user)) {
            githubClient.openIssue(repo, getTitle(), getBody(), user.getGithubLogin());
        }
    }

    // TODO remove this temporary method!
    private boolean isSystemUser(User user) {
        return user.getGithubLogin().equals("AnatoliiStepaniuk") || user.getGithubLogin().equals("chebotiuk");
    }

    private boolean issueExists(String repo) {
        return githubClient.getRepoIssues(repo, true).stream()
                .anyMatch(i -> i.getTitle().equals(getTitle()));
    }

    // TODO temporary
    private String getBody() {
        return "Заполни свой профиль https://betterhire.me/profile чтобы рекрутеры могли с тобой связаться"; // TODO
    }

    // TODO temporary
    private String getTitle() {
        return "Заполнить профиль на BetterHire.me";
    }

    private boolean isFilled(User user) {
        return user.getCity() != null && !user.getCity().isEmpty()
                && user.getCv() != null && !user.getCv().isEmpty();
    }

}
