package com.starscreen.entity;

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

    @Column(length = 500)
    private String poster;

    private Double score;

    @Column(length = 50)
    private String tag;

    @Column(length = 50)
    private String releaseDate;

    @Column(length = 20)
    private String status;

    private Integer wantCount;

    /** 导演（新增） */
    @Column(length = 100)
    private String director;

    /** 主演，逗号分隔（新增） */
    @Column(length = 500)
    private String actors;

    /** 时长，分钟（新增） */
    private Integer duration;
}