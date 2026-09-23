package com.maoyan.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long scheduleId;
    /** 形如 ["1排3座", "1排4座"] */
    private List<String> seats;
}
