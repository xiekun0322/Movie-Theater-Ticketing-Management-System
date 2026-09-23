package com.maoyan.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long scheduleId;
    private List<String> seats;
}
