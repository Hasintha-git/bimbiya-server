package com.bimbiya.server.dto;

import lombok.Data;

@Data
public class ResponseDTO {

    private String responseCode;

    private String responseDescription;

    private String redirectTarget;

    private Long fullCount;

    private Object data;
}
