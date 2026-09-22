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

    private String title;
    private String poster;
    private Double score;
    private String tag;
    private String releaseDate;
    private String status;
    private Integer wantCount;
}
