package com.sdehunt.dto;

import lombok.Data;

@Data
public class SaveSolutionDTO {

    private String userId;

    private String repo;

    private String commit;

}
