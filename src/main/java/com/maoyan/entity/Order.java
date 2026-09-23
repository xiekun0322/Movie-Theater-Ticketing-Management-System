package com.maoyan.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 订单实体
 * 注意：order 是 MySQL 保留字，表名用 t_order
 */
@Data
@Entity
@Table(name = "t_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String orderNo;      // 订单号

    private Long scheduleId;     // 场次 ID
    private Long movieId;        // 电影 ID

    @Column(length = 100)
    private String movieTitle;   // 电影名

    @Column(length = 100)
    private String cinemaName;   // 影院名

    @Column(length = 50)
    private String hallName;     // 影厅

    @Column(length = 50)
    private String showTime;     // 放映时间

    @Column(length = 500)
    private String seats;        // 座位："1排3座,1排4座"

    private Double totalPrice;   // 总价

    /** pending：待支付  paid：已支付  cancelled：已取消 */
    @Column(length = 20)
    private String status;

    @Column(length = 30)
    private String createTime;   // 下单时间

    @Column(length = 30)
    private String payTime;      // 支付时间（新增）

    @Column(length = 20)
    private String ticketCode;   // 取票码（新增）
}