package com.starscreen.controller;

import com.starscreen.entity.Movie;
import com.starscreen.entity.Schedule;
import com.starscreen.repository.MovieRepository;
import com.starscreen.repository.OrderRepository;
import com.starscreen.repository.ScheduleRepository;
import com.starscreen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PageController {

    @Autowired private MovieRepository movieRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private AdminService adminService;

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "PageController 工作正常";
    }

    /** 首页（支持搜索） */
    @GetMapping("/")
    public String index(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 有搜索关键词 → 只显示搜索结果
            List<Movie> searchResults = movieRepository.findByTitleContaining(keyword.trim());
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("keyword", keyword);
            model.addAttribute("isSearching", true);
        } else {
            // 无关键词 → 显示默认列表
            List<Movie> showingMovies = movieRepository.findByStatus("showing");
            List<Movie> upcomingMovies = movieRepository.findByStatus("upcoming");
            model.addAttribute("showingMovies", showingMovies);
            model.addAttribute("upcomingMovies", upcomingMovies);
            model.addAttribute("isSearching", false);
        }
        return "index";
    }

    /** 电影详情页 */
    @GetMapping("/movie/detail/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieRepository.findById(id).orElse(null));
        return "movie-detail";
    }

    @GetMapping("/cinemas/{movieId}")
    public String cinemas(@PathVariable Long movieId, Model model) {
        model.addAttribute("movie", movieRepository.findById(movieId).orElse(null));
        model.addAttribute("schedules", scheduleRepository.findByMovieId(movieId));
        return "cinemas";
    }

    @GetMapping("/seat/{scheduleId}")
    public String seat(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        model.addAttribute("schedule", schedule);
        if (schedule != null) {
            model.addAttribute("movie", movieRepository.findById(schedule.getMovieId()).orElse(null));
        }
        return "seat";
    }

    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).orElse(null));
        return "movie-order";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(required = false, defaultValue = "all") String status,
                         Model model) {
        List<com.starscreen.entity.Order> orders;
        if ("all".equals(status)) {
            orders = orderRepository.findAllByOrderByIdDesc();
        } else {
            orders = orderRepository.findByStatusOrderByIdDesc(status);
        }
        model.addAttribute("orders", orders);
        model.addAttribute("currentStatus", status);
        return "orders";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("stats", adminService.getStats());
        return "admin";
    }
}