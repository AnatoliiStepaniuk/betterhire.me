package com.sdehunt.score.cars;

import com.sdehunt.commons.FileUtils;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.score.GeneralFilesDownloader;
import com.sdehunt.score.TaskScoreCounter;

import java.util.Arrays;
import java.util.List;

public class CarsScoreCounter implements TaskScoreCounter {

    private final List<String> inputFiles = Arrays.asList("cars/input/a_example.in", "cars/input/b_should_be_easy.in", "cars/input/c_no_hurry.in", "cars/input/d_metropolis.in", "cars/input/e_high_bonus.in");
    private final List<String> solutionFiles = Arrays.asList("solutions/a_example.out", "solutions/b_should_be_easy.out", "solutions/c_no_hurry.out", "solutions/d_metropolis.out", "solutions/e_high_bonus.out");
    private final GeneralFilesDownloader filesDownloader;
    private final RidesReader ridesReader = new RidesReader();
    private final ConditionsReader conditionsReader = new ConditionsReader();
    private final TripsReader tripsReader = new TripsReader();
    private final TripScoreCounter tripScoreCounter = new TripScoreCounter();

    public CarsScoreCounter(GeneralFilesDownloader filesDownloader) {
        this.filesDownloader = filesDownloader;
    }

    @Override
    public long count(String userId, String repo, String commit) throws CommitOrFileNotFoundException {

        filesDownloader.downloadInputFiles(inputFiles);
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
