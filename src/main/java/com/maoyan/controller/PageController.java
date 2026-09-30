package com.maoyan.controller;

import com.maoyan.entity.Movie;
import com.maoyan.entity.Schedule;
import com.maoyan.repository.MovieRepository;
import com.maoyan.repository.OrderRepository;
import com.maoyan.repository.ScheduleRepository;
import com.maoyan.service.AdminService;
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

    @GetMapping("/")
    public String index(Model model) {
        List<Movie> showingMovies = movieRepository.findByStatus("showing");
        List<Movie> upcomingMovies = movieRepository.findByStatus("upcoming");
        model.addAttribute("showingMovies", showingMovies);
        model.addAttribute("upcomingMovies", upcomingMovies);
        return "index";
    }

    /** 电影详情页 */
    @GetMapping("/movie/detail/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieRepository.findById(id).orElse(null));
        return "movie-detail";           // ← 电影详情
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

    /** 订单详情 / 支付页 */
    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).orElse(null));
        return "movie-order";            // ← 订单详情（注意不是 movie-detail）
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(required = false, defaultValue = "all") String status,
                         Model model) {
        List<com.maoyan.entity.Order> orders;
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