package com.starscreen.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminStats {

    private long totalOrders;
    private long pendingOrders;
    private long paidOrders;
    private long cancelledOrders;
    private Double totalRevenue;
    private List<Object[]> topMovies;

    /** 按天票房（新增） */
    private List<Object[]> dailyRevenue;
}
