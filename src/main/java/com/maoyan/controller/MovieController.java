package com.maoyan.controller; // 声明当前类所在的包

import com.maoyan.entity.Movie; // 导入 Movie 实体
import com.maoyan.repository.MovieRepository; // 导入 MovieRepository
import org.springframework.beans.factory.annotation.Autowired; // 导入 @Autowired
import org.springframework.web.bind.annotation.*; // 导入 Spring MVC 注解

import java.util.List; // 导入 List

/**
 * 电影接口控制器
 * 提供 JSON 接口，供前端 AJAX 调用
 */
@RestController // RESTful 控制器，返回 JSON
@RequestMapping("/api/movies") // 路径前缀
@CrossOrigin // 允许跨域
public class MovieController {

    @Autowired // 注入电影仓库
    private MovieRepository movieRepository;

    /** 查询全部电影 */
    @GetMapping
    public List<Movie> list() {
        return movieRepository.findAll();
    }

    /** 按状态查询电影 */
    @GetMapping("/status")
    public List<Movie> listByStatus(@RequestParam String status) {
        return movieRepository.findByStatus(status);
    }

    /** 查询单部电影 */
    @GetMapping("/{id}")
    public Movie detail(@PathVariable Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    /** 新增电影 */
    @PostMapping
    public Movie add(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    /** 修改电影 */
    @PutMapping("/{id}")
    public Movie update(@PathVariable Long id, @RequestBody Movie movie) {
        movie.setId(id);
        return movieRepository.save(movie);
    }

    /** 删除电影 */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieRepository.deleteById(id);
    }
}
