package com.sdehunt.commons.util;

public class RepoUtils {

    public static String trimRepo(String repo) {
        int first = repo.lastIndexOf("/");
        int last = repo.lastIndexOf(".git");
        if (last < 0) {
            last = repo.length();
        }
        return repo.substring(first + 1, last);
    }

}
