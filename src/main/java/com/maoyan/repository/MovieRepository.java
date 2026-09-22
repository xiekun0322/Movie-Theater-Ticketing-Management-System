package com.maoyan.repository; // 声明当前类所在的包

import com.maoyan.entity.Movie; // 导入 Movie 实体
import org.springframework.data.jpa.repository.JpaRepository; // 导入 JPA 仓库接口

import java.util.List; // 导入 List

/**
 * 电影仓库接口
 * 继承 JpaRepository 后，自动获得 findAll / findById / save / deleteById 等方法
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * 按状态查询电影
     * 自动生成 SQL：SELECT * FROM movie WHERE status = ?
     *
     * @param status 电影状态（showing / upcoming）
     * @return 符合条件的电影列表
     */
    List<Movie> findByStatus(String status);
}
