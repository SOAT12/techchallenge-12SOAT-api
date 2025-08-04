package com.fiap.soat12.tc_group_7.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

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
