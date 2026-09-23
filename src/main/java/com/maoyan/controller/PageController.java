package com.maoyan.controller;

import com.maoyan.entity.Movie;
import com.maoyan.entity.Schedule;
import com.maoyan.repository.MovieRepository;
import com.maoyan.repository.OrderRepository;
import com.maoyan.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PageController {

    @Autowired private MovieRepository movieRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private OrderRepository orderRepository;

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

    @GetMapping("/movie/detail/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        model.addAttribute("movie", movie);
        return "movie-detail";
    }

    @GetMapping("/cinemas/{movieId}")
    public String cinemas(@PathVariable Long movieId, Model model) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        List<Schedule> schedules = scheduleRepository.findByMovieId(movieId);
        model.addAttribute("movie", movie);
        model.addAttribute("schedules", schedules);
        return "cinemas";
    }

    @GetMapping("/seat/{scheduleId}")
    public String seat(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        model.addAttribute("schedule", schedule);

        if (schedule != null) {
            Movie movie = movieRepository.findById(schedule.getMovieId()).orElse(null);
            model.addAttribute("movie", movie);
        }
        return "seat";
    }

    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).orElse(null));
        return "movie-order";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderRepository.findAllByOrderByIdDesc());
        return "orders";
    }
}
