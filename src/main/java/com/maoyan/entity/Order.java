package com.maoyan.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String orderNo;

    private Long scheduleId;
    private Long movieId;

    @Column(length = 100)
    private String movieTitle;

    @Column(length = 100)
    private String cinemaName;

    @Column(length = 50)
    private String hallName;

    @Column(length = 50)
    private String showTime;

    @Column(length = 500)
    private String seats;

    private Double totalPrice;

    @Column(length = 20)
    private String status;

    @Column(length = 30)
    private String createTime;

    @Column(length = 30)
    private String payTime;

    @Column(length = 20)
    private String ticketCode;
}