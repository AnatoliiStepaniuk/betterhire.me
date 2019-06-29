package com.sdehunt.score.pizza;

import com.sdehunt.commons.FileUtils;
import com.sdehunt.commons.github.exceptions.CommitOrFileNotFoundException;
import com.sdehunt.score.GeneralFilesDownloader;
import com.sdehunt.score.TaskScoreCounter;

import java.util.Arrays;
import java.util.List;

public class PizzaScoreCounter implements TaskScoreCounter {

    private final List<String> inputFiles = Arrays.asList("pizza/input/example.in", "pizza/input/small.in", "pizza/input/medium.in", "pizza/input/big.in");
    private final List<String> solutionFiles = Arrays.asList("solutions/example.out", "solutions/small.out", "solutions/medium.out", "solutions/big.out");
    private final GeneralFilesDownloader filesDownloader;
    private final PizzaReader pizzaReader = new PizzaReaderImpl();
    private final SliceReader sliceReader = new EagerSliceReader();
    private final PizzaSliceChecker checker = new PizzaSliceChecker();

    public PizzaScoreCounter(GeneralFilesDownloader filesDownloader) {
        this.filesDownloader = filesDownloader;
    }

    @Override
    public long count(String userId, String repo, String commit) throws CommitOrFileNotFoundException {

        filesDownloader.downloadInputFiles(inputFiles);
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
