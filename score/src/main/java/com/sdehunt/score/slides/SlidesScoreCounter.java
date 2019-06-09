package com.sdehunt.score.slides;

import com.sdehunt.commons.FileUtils;
import com.sdehunt.commons.exception.InvalidSolutionException;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.score.TaskScoreCounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Calculates score for hashcode Slides tasks
 */
public class SlidesScoreCounter implements TaskScoreCounter {

    private final PicturesReader picturesReader = new EagerPicturesReader();

    private static final String INPUT_REPO = "AnatoliiStepaniuk/google_hash_code_2019";
    private static final String INPUT_BRANCH = "master";
    private List<String> inputFiles;
    private List<String> solutionFiles;

    private final GithubClient githubClient;

    public SlidesScoreCounter(GithubClient githubClient) {
        this(
                githubClient,
                Arrays.asList("input/a_input.txt", "input/b_input.txt", "input/c_input.txt", "input/d_input.txt", "input/e_input.txt"),
                Arrays.asList("solutions/a_result.txt", "solutions/b_result.txt", "solutions/c_result.txt", "solutions/d_result.txt", "solutions/e_result.txt")
        );
    }

    public SlidesScoreCounter(GithubClient githubClient, List<String> inputFiles, List<String> solutionFiles) {
        this.githubClient = githubClient;
        this.inputFiles = inputFiles;
        this.solutionFiles = solutionFiles;
    }

    public static SlidesScoreCounter test(GithubClient githubClient) {
        return new SlidesScoreCounter(
                githubClient,
                Collections.singletonList("input/a_input.txt"),
                Collections.singletonList("solutions/a_result.txt")
        );
    }

    @Override
    public long count(String repo, String commit) throws CommitOrFileNotFoundException {

        for (String f : inputFiles) { // TODO it once CACHE IT SOMEHOW
            githubClient.download(INPUT_REPO, INPUT_BRANCH, f);// TODO name files so that it does not clunch with files for other tasks
        }
        for (String f : solutionFiles) {
            githubClient.download(repo, commit, f); // TODO solution files downloader
        } // TODO clean solution files after finishing

        long score = 0;
        for (int i = 0; i < inputFiles.size(); i++) {
            Map<Integer, Picture> pictures = picturesReader.readPictures(FileUtils.fileName(inputFiles.get(i)));
            try {
                List<String> lines = Files.readAllLines(Paths.get(FileUtils.fileName(solutionFiles.get(i))));
                if (lines.isEmpty()) {
                    throw new InvalidSolutionException();
                }

                for (int l = 1; l < lines.size() - 1; l++) {
                    score += countScore(getTags(pictures, lines.get(l)), getTags(pictures, lines.get(l + 1)));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return score;
    } // TODO add unit tests

    private Set<String> getTags(Map<Integer, Picture> pictures, String line) {
        if (line.split(" ").length == 2) { // TODO separate method
            Integer firstIndex = Integer.valueOf(line.split(" ")[0]);
            Integer secondIndex = Integer.valueOf(line.split(" ")[1]);
            verifyBothVertical(pictures.get(firstIndex), pictures.get(secondIndex));
            Set<String> allTags = new HashSet<>();
            allTags.addAll(pictures.get(firstIndex).getTags());
            allTags.addAll(pictures.get(secondIndex).getTags());
            return allTags;
        }

        return pictures.get(Integer.valueOf(line)).getTags();
    }

    private void verifyBothVertical(Picture first, Picture second) {
        if (first.isVertical() && second.isVertical()) {
            return;
        }
        throw new RuntimeException("Both pictures should be vertical");
    }

    long countScore(Set<String> first, Set<String> second) {

        HashSet<String> left = new HashSet<>(first);
        left.removeAll(second);


        HashSet<String> right = new HashSet<>(second);
        right.removeAll(first);

        HashSet<String> middle = new HashSet<>(first);
        middle.retainAll(second);

        return min(left.size(), middle.size(), right.size());
    }

    long min(long l1, long l2, long l3) {
        return Math.min(l1, Math.min(l2, l3));
    }

}