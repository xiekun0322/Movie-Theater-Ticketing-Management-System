package com.maoyan.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 座位实体
 */
@Data
@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long scheduleId;  // 所属场次
    private Integer rowNum;   // 排号
    private Integer colNum;   // 列号

    /** available：可选  locked：被待支付订单锁定  sold：已售 */
    @Column(length = 20)
    private String status;

    private Long orderId;     // 锁定/售出该座位的订单 ID
}
