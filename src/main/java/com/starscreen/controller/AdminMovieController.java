package com.starscreen.controller;

import com.starscreen.common.Result;
import com.starscreen.entity.Movie;
import com.starscreen.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/movies")
@CrossOrigin
public class AdminMovieController {

    @Autowired
    private MovieService movieService;

    /** 分页列表 */
    @GetMapping
    public Result<Page<Movie>> list(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return Result.success(movieService.listPaged(page, size));
    }

    /** 详情 */
    @GetMapping("/{id}")
    public Result<Movie> detail(@PathVariable Long id) {
        return Result.success(movieService.getById(id));
    }

    /** 新增 */
    @PostMapping
    public Result<Movie> create(@RequestBody Movie movie) {
        return Result.success(movieService.create(movie));
    }

    /** 更新 */
    @PutMapping("/{id}")
    public Result<Movie> update(@PathVariable Long id, @RequestBody Movie movie) {
        return Result.success(movieService.update(id, movie));
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return Result.success();
    }
}
