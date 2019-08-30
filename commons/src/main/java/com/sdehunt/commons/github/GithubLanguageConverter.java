package com.sdehunt.commons.github;

import com.sdehunt.commons.model.Language;

/**
 * Converts languages from Github format to BetterHire format
 */
public class GithubLanguageConverter {

    public static Language convert(String githubLang) {
        switch (githubLang.toLowerCase()) {
            case "javascript":
                return Language.JS;
            case "java":
                return Language.JAVA;
            case "c++":
                return Language.CPP;
            case "python":
                return Language.PYTHON;
            default:
                return Language.OTHER;
        }
    }

}
