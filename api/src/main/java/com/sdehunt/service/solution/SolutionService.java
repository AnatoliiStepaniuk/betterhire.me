package com.sdehunt.service.solution;

import com.sdehunt.commons.model.Solution;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.score.GeneralScoreCounter;

public class SolutionService {

    private GeneralScoreCounter scoreCounter;

    private SolutionRepository solutionRepository;

    public SolutionService(GeneralScoreCounter scoreCounter, SolutionRepository solutionRepository) {
        this.scoreCounter = scoreCounter;
        this.solutionRepository = solutionRepository;
    }

    public long calculateScoreAndSave(Solution s){
        long score = scoreCounter.count(s);
        solutionRepository.save(s.setScore(score)); // TODO resolve commit (if master)
        return score;
    }
}
