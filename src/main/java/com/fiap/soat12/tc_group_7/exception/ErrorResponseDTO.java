package com.fiap.soat12.tc_group_7.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String title;
    private String detail;
    private String path;

    public ErrorResponseDTO(int status, String title, String detail, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.path = path;
    }
}
