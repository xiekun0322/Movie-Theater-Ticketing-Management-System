package com.starscreen.repository;

import com.starscreen.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    /** 按状态查询 */
    List<Movie> findByStatus(String status);

    /** 按标题模糊查询（搜索用） */
    List<Movie> findByTitleContaining(String keyword);
}