package com.starscreen.dto;

import lombok.Data;

@Data
public class SeatVO {
    private Long id;
    private Integer rowNum;
    private Integer colNum;
    private String status;
    private String label;
}
