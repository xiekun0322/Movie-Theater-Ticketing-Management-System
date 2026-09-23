package com.maoyan.dto;

import lombok.Data;

@Data
public class SeatVO {
    private Long id;
    private Integer rowNum;
    private Integer colNum;
    private String status;  // available / locked / sold
    private String label;   // "1排3座"
}
