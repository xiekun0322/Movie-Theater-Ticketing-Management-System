package com.maoyan.repository; // 声明当前类所在的包

import com.maoyan.entity.Schedule; // 导入 Schedule 实体
import org.springframework.data.jpa.repository.JpaRepository; // 导入 JPA 仓库接口

import java.util.List; // 导入 List

/**
 * 场次仓库接口
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /** 按电影 ID 查询场次 */
    List<Schedule> findByMovieId(Long movieId);

    /** 按电影 ID 和影院 ID 查询场次 */
    List<Schedule> findByMovieIdAndCinemaId(Long movieId, Long cinemaId);

    /** 按电影 ID 和日期查询场次 */
    List<Schedule> findByMovieIdAndDate(Long movieId, String date);
}
