package com.maoyan.controller; // 声明当前类所在的包

import com.maoyan.entity.Schedule; // 导入 Schedule 实体
import com.maoyan.repository.ScheduleRepository; // 导入 ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired; // 导入 @Autowired
import org.springframework.web.bind.annotation.*; // 导入注解

import java.util.List; // 导入 List

/**
 * 场次接口控制器
 * 提供 JSON 接口，供前端 AJAX 调用
 */
@RestController // RESTful 控制器
@RequestMapping("/api/schedules") // 路径前缀
@CrossOrigin // 允许跨域
public class ScheduleController {

    @Autowired // 注入场次仓库
    private ScheduleRepository scheduleRepository;

    /** 查询全部场次 */
    @GetMapping
    public List<Schedule> list() {
        return scheduleRepository.findAll();
    }

    /** 按电影 ID 和日期查询场次 */
    @GetMapping("/search")
    public List<Schedule> search(@RequestParam(required = false) Long movieId,
                                 @RequestParam(required = false) String date) {
        if (movieId != null && date != null) {
            return scheduleRepository.findByMovieIdAndDate(movieId, date);
        }
        if (movieId != null) {
            return scheduleRepository.findByMovieId(movieId);
        }
        return scheduleRepository.findAll();
    }

    /** 查询单个场次 */
    @GetMapping("/{id}")
    public Schedule detail(@PathVariable Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }
}
