package com.bimbiya.server.dto.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String errorCode;

    private String errorDescription;


    private String errorComponent;

    private Object data;
}
