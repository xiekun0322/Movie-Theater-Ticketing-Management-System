package com.maoyan.controller;

import com.maoyan.entity.Movie;
import com.maoyan.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    /** 查询全部电影 */
    @GetMapping
    public List<Movie> list() {
        return movieRepository.findAll();
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
