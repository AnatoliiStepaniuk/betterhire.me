package com.sdehunt.score.cities;

import com.sdehunt.commons.exception.InvalidSolutionException;
import com.sdehunt.commons.util.FileUtils;
import com.sdehunt.score.GeneralFilesDownloader;
import com.sdehunt.score.TaskScoreCounter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CitiesScoreCounter implements TaskScoreCounter {

    private final String inputFileS3 = "cities/input.txt";
    private final String solutionFile = "output.txt";

    private final GeneralFilesDownloader filesDownloader;

    public CitiesScoreCounter(GeneralFilesDownloader filesDownloader) {
        this.filesDownloader = filesDownloader;
    }

    @SneakyThrows
    @Override
    public long count(String userId, String repo, String commit) {

        filesDownloader.downloadInputFile(inputFileS3);
        filesDownloader.downloadSolutionFile(userId, repo, commit, solutionFile);

        String inputFile = FileUtils.fileName(inputFileS3);
        List<String> availableCities = Files.readAllLines(Paths.get(inputFile)).stream()
                .filter(l -> !l.trim().isEmpty())
                .collect(Collectors.toList());
        List<String> citiesList = Files.readAllLines(Paths.get(solutionFile)).stream()
                .filter(l -> !l.trim().isEmpty())
                .collect(Collectors.toList());

        return checkAndCountScore(citiesList, availableCities);
    }

    long checkAndCountScore(List<String> citiesList, List<String> availableCities) {
        HashSet<String> availableCitiesSet = new HashSet<>(availableCities);
        if (!availableCitiesSet.remove(citiesList.get(0))) {
            throw new InvalidSolutionException("City \"" + citiesList.get(0) + "\" is not present in list of available cities");
        }
        long count = citiesList.get(0).length();

        for (int i = 1; i < citiesList.size(); i++) {
            String prev = citiesList.get(i - 1);
            String city = citiesList.get(i);

            if (!city.substring(0, 1).toLowerCase().equals(prev.substring(prev.length() - 1))) {
                throw new InvalidSolutionException("City \"" + city + "\" cannot follow \"" + prev + "\"");
            }

            if (!availableCitiesSet.remove(city)) {
                throw new InvalidSolutionException("City \"" + city + "\" is not present in list of available cities");
            }

            count += city.length();

        }

        return count;
    }

}
