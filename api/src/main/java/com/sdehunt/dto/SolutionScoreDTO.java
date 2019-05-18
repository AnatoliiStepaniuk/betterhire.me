package com.sdehunt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolutionScoreDTO {
    private String solutionId;
    private long score;
}
