package com.starscreen.controller;

import com.starscreen.entity.Movie;
import com.starscreen.entity.Order;
import com.starscreen.entity.Schedule;
import com.starscreen.repository.MovieRepository;
import com.starscreen.repository.OrderRepository;
import com.starscreen.repository.ScheduleRepository;
import com.starscreen.service.AdminService;
import com.starscreen.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Autowired private OrderService orderService;
    @Autowired private AdminService adminService;

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "PageController 工作正常";
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Movie> searchResults = movieRepository.findByTitleContaining(keyword.trim());
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("keyword", keyword);
            model.addAttribute("isSearching", true);
        } else {
            List<Movie> showingMovies = movieRepository.findByStatus("showing");
            List<Movie> upcomingMovies = movieRepository.findByStatus("upcoming");
            model.addAttribute("showingMovies", showingMovies);
            model.addAttribute("upcomingMovies", upcomingMovies);
            model.addAttribute("isSearching", false);
        }
        return "index";
    }

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
    public String orderDetail(@PathVariable Long id, Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return "redirect:/orders";
        if (userId == null || !userId.equals(order.getUserId())) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "movie-order";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(required = false, defaultValue = "all") String status,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Page<Order> orderPage = orderService.listPagedByUser(userId, status, page, size);
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentPage", page);
        return "orders";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("stats", adminService.getStats());
        return "admin";
    }

    /** 电影管理页 */
    @GetMapping("/admin/movies")
    public String adminMovies() {
        return "admin-movies";
    }

    /** 场次管理页 */
    @GetMapping("/admin/schedules")
    public String adminSchedules(@RequestParam(required = false) Long movieId, Model model) {
        model.addAttribute("movieId", movieId);
        model.addAttribute("movies", movieRepository.findAll());
        return "admin-schedules";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}