package com.starscreen.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotNull(message = "场次 ID 不能为空")
    private Long scheduleId;

    @NotEmpty(message = "请至少选择一个座位")
    @Size(max = 6, message = "一次最多选择 6 个座位")
    private List<String> seats;
}