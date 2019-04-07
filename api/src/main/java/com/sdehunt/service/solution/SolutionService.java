package com.sdehunt.service.solution;

import com.sdehunt.model.Solution;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.score.GeneralScoreCounter;
import org.springframework.beans.factory.annotation.Autowired;

public class SolutionService {

    @Autowired
    private GeneralScoreCounter scoreCounter;

    @Autowired
    private SolutionRepository solutionRepository;

    public long calculateScoreAndSave(Solution s){
        long score = scoreCounter.count(s.getTaskId(), s.getRepo(), s.getCommit());
        solutionRepository.save(s.withScore(score));
        return score;
    }
}
