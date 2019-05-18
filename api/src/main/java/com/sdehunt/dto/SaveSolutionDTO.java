package com.sdehunt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveSolutionDTO {

    private String userId;

    private String repo;

    private String commit;

}
