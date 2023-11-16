package com.bimbiya.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTableDTO<T> {

    private Long totalRecords;
    private Long filteredRecords;
    private List<T> records;
    private Integer draw;



}