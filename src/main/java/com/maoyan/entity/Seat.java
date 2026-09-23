package com.maoyan.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long scheduleId;
    private Integer rowNum;
    private Integer colNum;

    @Column(length = 20)
    private String status;

    private Long orderId;
}
