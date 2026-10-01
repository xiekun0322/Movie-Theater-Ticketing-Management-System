package com.starscreen.repository;

import com.starscreen.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByStatus(String status);

    List<Movie> findByTitleContaining(String keyword);

    /** 分页查询（管理端用） */
    Page<Movie> findAllByOrderByIdDesc(Pageable pageable);
}