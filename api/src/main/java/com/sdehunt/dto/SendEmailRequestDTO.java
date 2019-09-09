package com.sdehunt.dto;

import lombok.Data;

@Data
public class SendEmailRequestDTO {
    private String templateId;
    private String sql;
}
