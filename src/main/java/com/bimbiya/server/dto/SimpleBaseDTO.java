package com.bimbiya.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleBaseDTO {
    private Long id;
    private String code;
    private String description;

    public SimpleBaseDTO(String code, String description){
        this.code=code;
        this.description=description;

    }
    public SimpleBaseDTO(Long id, String description){
        this.id=id;
        this.description=description;

    }


}
