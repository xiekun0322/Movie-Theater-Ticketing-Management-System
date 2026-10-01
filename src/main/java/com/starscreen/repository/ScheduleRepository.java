package com.starscreen.repository;

import com.starscreen.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /** 按电影 ID 查询（前台选场次用） */
    List<Schedule> findByMovieId(Long movieId);

    /** 按电影 ID + 影院 ID 查询 */
    List<Schedule> findByMovieIdAndCinemaId(Long movieId, Long cinemaId);

    /** 按电影 ID + 日期查询 */
    List<Schedule> findByMovieIdAndDate(Long movieId, String date);

    /** 管理端：按电影 ID 分页查询（新增） */
    Page<Schedule> findByMovieIdOrderByIdDesc(Long movieId, Pageable pageable);

    /** 管理端：全部分页（新增） */
    Page<Schedule> findAllByOrderByIdDesc(Pageable pageable);
}
