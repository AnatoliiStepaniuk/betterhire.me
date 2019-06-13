package com.sdehunt.score.cars;

import com.sdehunt.commons.FileUtils;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.score.FilesDownloader;
import com.sdehunt.score.TaskScoreCounter;

import java.util.Arrays;
import java.util.List;

public class CarsScoreCounter implements TaskScoreCounter {

    private static final String INPUT_REPO = "AnatoliiStepaniuk/sdehunt_input";
    private static final String INPUT_BRANCH = "master";

    private final List<String> inputFiles = Arrays.asList("cars/a_example.in", "cars/b_should_be_easy.in", "cars/c_no_hurry.in", "cars/d_metropolis.in", "cars/e_high_bonus.in");
    private final List<String> solutionFiles = Arrays.asList("solutions/a_example.out", "solutions/b_should_be_easy.out", "solutions/c_no_hurry.out", "solutions/d_metropolis.out", "solutions/e_high_bonus.out");
    private final FilesDownloader filesDownloader;
    private final RidesReader ridesReader = new RidesReader();
    private final ConditionsReader conditionsReader = new ConditionsReader();
    private final TripsReader tripsReader = new TripsReader();
    private final TripScoreCounter tripScoreCounter = new TripScoreCounter();

    public CarsScoreCounter(FilesDownloader filesDownloader) {
        this.filesDownloader = filesDownloader;
    }

    @Override
    public long count(String userId, String repo, String commit) throws CommitOrFileNotFoundException {

        filesDownloader.downloadInputFiles(INPUT_REPO, INPUT_BRANCH, inputFiles);
        filesDownloader.downloadSolutionFiles(userId, repo, commit, solutionFiles);

        int count = 0;
        for (int i = 0; i < inputFiles.size(); i++) {
            Conditions c = conditionsReader.read(FileUtils.fileName(inputFiles.get(i)));
            List<Ride> rides = ridesReader.read(FileUtils.fileName(inputFiles.get(i)));
            List<Trip> trips = tripsReader.read(FileUtils.fileName(solutionFiles.get(i)), rides, c.getVehicles());
            count += tripScoreCounter.count(trips, c.getBonus());
        }

        // TODO remove files

        return count;
    }
}
