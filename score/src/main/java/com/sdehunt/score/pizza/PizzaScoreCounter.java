package com.sdehunt.score.pizza;

import com.sdehunt.commons.FileUtils;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.score.FilesDownloader;
import com.sdehunt.score.TaskScoreCounter;

import java.util.Arrays;
import java.util.List;

public class PizzaScoreCounter implements TaskScoreCounter {

    private static final String INPUT_REPO = "AnatoliiStepaniuk/sdehunt_input";
    private static final String INPUT_BRANCH = "master";

    private final List<String> inputFiles = Arrays.asList("pizza/example.in", "pizza/small.in", "pizza/medium.in", "pizza/big.in");
    private final List<String> solutionFiles = Arrays.asList("solutions/example.out", "solutions/small.out", "solutions/medium.out", "solutions/big.out");
    private final FilesDownloader filesDownloader;
    private final PizzaReader pizzaReader = new PizzaReaderImpl();
    private final SliceReader sliceReader = new EagerSliceReader();
    private final PizzaSliceChecker checker = new PizzaSliceChecker();

    public PizzaScoreCounter(FilesDownloader filesDownloader) {
        this.filesDownloader = filesDownloader;
    }

    @Override
    public long count(String userId, String repo, String commit) throws CommitOrFileNotFoundException {

        filesDownloader.downloadInputFiles(INPUT_REPO, INPUT_BRANCH, inputFiles);
        filesDownloader.downloadSolutionFiles(userId, repo, commit, solutionFiles);

        int count = 0;
        for (int i = 0; i < inputFiles.size(); i++) {
            Pizza pizza = pizzaReader.read(FileUtils.fileName(inputFiles.get(i)));
            count += sliceReader.read(FileUtils.fileName(solutionFiles.get(i)))
                    .stream().map(s -> count(pizza, s))
                    .reduce(Integer::sum)
                    .orElse(0);
        }

        // TODO remove files

        return count;
    }

    private int count(Pizza pizza, Slice slice) {
        checker.check(pizza, slice);
        pizza.remove(slice);
        return slice.area();
    }

}
