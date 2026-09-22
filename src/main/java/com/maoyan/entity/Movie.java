package com.maoyan.entity; // 声明当前类所在的包

import jakarta.persistence.*; // 导入 JPA 注解
import lombok.Data; // 导入 Lombok 的 @Data

/**
 * 电影实体类
 * 对应数据库中的 movie 表
 */
@Data // Lombok 自动生成 getter/setter/toString
@Entity // 声明为 JPA 实体
@Table(name = "movie") // 指定映射的表名
public class Movie {

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Long id; // 电影 ID

    private String title;       // 电影名称
    private String poster;      // 海报（渐变色 CSS 字符串）
    private Double score;       // 评分
    private String tag;         // 标签（如 2DIMAX、3D）
    private String releaseDate; // 上映日期
    private String status;      // 状态：showing（正在热映）/ upcoming（即将上映）
    private Integer wantCount;  // 想看人数
}
