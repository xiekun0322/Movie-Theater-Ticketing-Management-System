package com.maoyan.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 500)  // 防止渐变色字符串过长
    private String poster;

    private Double score;

    @Column(length = 50)
    private String tag;

    @Column(length = 50)
    private String releaseDate;

    @Column(length = 20)   // showing / upcoming
    private String status;

    private Integer wantCount;
}
