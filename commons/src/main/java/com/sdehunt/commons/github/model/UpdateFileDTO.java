package com.sdehunt.commons.github.model;

import lombok.Data;

@Data
public class UpdateFileDTO extends CreateFileDTO {

    private String sha;

}
