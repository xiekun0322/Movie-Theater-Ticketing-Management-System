package com.starscreen.service;

import com.starscreen.common.BusinessException;
import com.starscreen.entity.Movie;
import com.starscreen.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepository movieRepository;

    // ==================== 查询 ====================
    public List<Movie> listAll() {
        return movieRepository.findAll();
    }

    public Page<Movie> listPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findAllByOrderByIdDesc(pageable);
    }

    public List<Movie> listByStatus(String status) {
        return movieRepository.findByStatus(status);
    }

    public Movie getById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    // ==================== 增删改 ====================
    @Transactional
    public Movie create(Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new BusinessException("电影名不能为空");
        }
        // 默认状态
        if (movie.getStatus() == null || movie.getStatus().isEmpty()) {
            movie.setStatus("showing");
        }
        // 默认海报
        if (movie.getPoster() == null || movie.getPoster().isEmpty()) {
            movie.setPoster("/images/poster1.jpg");
        }
        movie = movieRepository.save(movie);
        log.info("新增电影：id={}, title={}", movie.getId(), movie.getTitle());
        return movie;
    }

    @Transactional
    public Movie update(Long id, Movie movie) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException("电影不存在"));

        if (movie.getTitle() != null) existing.setTitle(movie.getTitle());
        if (movie.getPoster() != null) existing.setPoster(movie.getPoster());
        if (movie.getScore() != null) existing.setScore(movie.getScore());
        if (movie.getTag() != null) existing.setTag(movie.getTag());
        if (movie.getReleaseDate() != null) existing.setReleaseDate(movie.getReleaseDate());
        if (movie.getStatus() != null) existing.setStatus(movie.getStatus());
        if (movie.getWantCount() != null) existing.setWantCount(movie.getWantCount());
        if (movie.getDirector() != null) existing.setDirector(movie.getDirector());
        if (movie.getActors() != null) existing.setActors(movie.getActors());
        if (movie.getDuration() != null) existing.setDuration(movie.getDuration());

        existing = movieRepository.save(existing);
        log.info("更新电影：id={}, title={}", id, existing.getTitle());
        return existing;
    }

    @Transactional
    public void delete(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException("电影不存在"));
        movieRepository.delete(movie);
        log.info("删除电影：id={}, title={}", id, movie.getTitle());
    }
}
