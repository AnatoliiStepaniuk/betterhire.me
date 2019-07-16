package com.sdehunt.score.letters;

import com.sdehunt.commons.util.FileUtils;
import com.sdehunt.score.GeneralFilesDownloader;
import com.sdehunt.score.TaskScoreCounter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneralLettersScoreCounter implements TaskScoreCounter {

    private final List<String> inputLettersFiles = Arrays.asList(
            "letters/input/a_letters.txt",
            "letters/input/b_letters.txt",
            "letters/input/c_letters.txt",
            "letters/input/d_letters.txt"
    );

    private final List<String> inputWordsFiles = Arrays.asList(
            "letters/input/a_words.txt",
            "letters/input/b_words.txt",
            "letters/input/c_words.txt",
            "letters/input/d_words.txt"
    );

    private final List<String> solutionFiles = Arrays.asList(
            "solutions/a_result.txt",
            "solutions/b_result.txt",
            "solutions/c_result.txt",
            "solutions/d_result.txt"
    );

    private final GeneralFilesDownloader filesDownloader;
    private final LettersReader lettersReader;
    private final LettersScoreCounter lettersScoreCounter;

    public GeneralLettersScoreCounter(GeneralFilesDownloader filesDownloader) {
        this.filesDownloader = filesDownloader;
        this.lettersReader = new LettersReader();
        this.lettersScoreCounter = new LettersScoreCounter();
    }

    @SneakyThrows
    @Override
    public long count(String userId, String repo, String commit) {

        filesDownloader.downloadInputFiles(inputLettersFiles);
        filesDownloader.downloadInputFiles(inputWordsFiles);
        filesDownloader.downloadSolutionFiles(userId, repo, commit, solutionFiles);

        long count = 0;
        for (int i = 0; i < inputLettersFiles.size(); i++) {
            String inputWordsFile = FileUtils.fileName(inputWordsFiles.get(i));
            String inputLettersFile = FileUtils.fileName(inputLettersFiles.get(i));
            String solutionsFile = FileUtils.fileName(solutionFiles.get(i));
            Map<Character, Integer> inputLetters = lettersReader.readLetters(FileUtils.fileName(inputLettersFiles.get(i)));
            List<String> inputWords = Files.readAllLines(Paths.get(FileUtils.fileName(inputWordsFiles.get(i)))).stream().map(String::trim).collect(Collectors.toList());
            List<String> solutionWords = Files.readAllLines(Paths.get(solutionsFile)).stream().map(String::trim).collect(Collectors.toList());

            count += lettersScoreCounter.count(inputWordsFile, inputLettersFile, inputLetters, inputWords, solutionWords);
        }

        return count;
    }
}
