package com.maoyan.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminStats {

    /** 订单总数 */
    private long totalOrders;

    /** 待支付订单数 */
    private long pendingOrders;

    /** 已支付订单数 */
    private long paidOrders;

    /** 已取消订单数 */
    private long cancelledOrders;

    /** 总票房 */
    private Double totalRevenue;

    /** 热销电影 TOP：每行 [movieTitle, totalRevenue, orderCount] */
    private List<Object[]> topMovies;
}
