package com.sdehunt.score.slides;

import com.sdehunt.score.TaskScoreCounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates score for hashcode Slides tasks
 */
public class SlidesScoreCounter implements TaskScoreCounter {

    private final PicturesReader picturesReader = new EagerPicturesReader();

    private static final String PICTURES_FILE = "/Users/anatolii.stepaniuk/Code/sdehunt/scoreworker/src/main/resources/a_input.txt";
    private static final String SOLUTION_FILE = "/Users/anatolii.stepaniuk/Code/sdehunt/scoreworker/src/main/resources/a_result.txt";

    @Override
    public long count(String repo, String commit) {

        // TODO use repo and commit

        Map<Integer, Picture> pictures = picturesReader.readPictures(PICTURES_FILE);
        long score = 0;
        try {
            List<String> lines = Files.readAllLines(Paths.get(SOLUTION_FILE));

            for (int i = 1; i < lines.size() - 1; i++) {
                score += countScore(getTags(pictures, lines.get(i)), getTags(pictures, lines.get(i + 1)));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return score;
    } // TODO add unit tests

    private Set<String> getTags(Map<Integer, Picture> pictures, String line) {
        if (line.split(" ").length == 2) {
            Integer firstIndex = Integer.valueOf(line.split(" ")[0]);
            Integer secondIndex = Integer.valueOf(line.split(" ")[1]);
            //verifyBothVertical(pictures.get(firstIndex), pictures.get(secondIndex)); // TODO uncomment
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
