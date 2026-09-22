package com.maoyan.entity; // 声明当前类所在的包

import jakarta.persistence.*; // 导入 JPA 注解
import lombok.Data; // 导入 Lombok 的 @Data

/**
 * 场次实体类
 * 对应数据库中的 schedule 表
 */
@Data // Lombok 自动生成 getter/setter/toString
@Entity // 声明为 JPA 实体
@Table(name = "schedule") // 指定映射的表名
public class Schedule {

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Long id; // 场次 ID

    private Long movieId;      // 电影 ID
    private Long cinemaId;     // 影院 ID
    private String cinemaName; // 影院名称
    private String hallName;   // 影厅名称（如"1号厅"、"IMAX厅"）
    private String startTime;  // 开始时间（如"10:30"）
    private String endTime;    // 散场时间（如"12:54"）
    private String language;   // 语言版本（如"国语 2D"）
    private Double price;      // 票价
    private String date;       // 放映日期（如"2026-09-22"）
}
