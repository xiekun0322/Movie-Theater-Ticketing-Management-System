package com.maoyan.controller; // 声明当前类所在的包

import com.maoyan.entity.Movie; // 导入 Movie 实体
import com.maoyan.entity.Schedule; // 导入 Schedule 实体
import com.maoyan.repository.MovieRepository; // 导入电影仓库
import com.maoyan.repository.OrderRepository; // 导入订单仓库
import com.maoyan.repository.ScheduleRepository; // 导入场次仓库
import org.springframework.beans.factory.annotation.Autowired; // 导入 @Autowired
import org.springframework.stereotype.Controller; // 导入 @Controller
import org.springframework.ui.Model; // 导入 Model
import org.springframework.web.bind.annotation.GetMapping; // 导入 @GetMapping
import org.springframework.web.bind.annotation.PathVariable; // 导入 @PathVariable
import org.springframework.web.bind.annotation.ResponseBody; // 导入 @ResponseBody

import java.util.List; // 导入 List

/**
 * 页面跳转控制器
 */
@Controller
public class PageController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private OrderRepository orderRepository;

    /** 诊断接口 */
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "PageController 工作正常";
    }

    /** 首页 */
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
        Movie movie = movieRepository.findById(id).orElse(null);
        model.addAttribute("movie", movie);
        return "movie-detail";
    }

    /** 选影院/选场次页 */
    @GetMapping("/cinemas/{movieId}")
    public String cinemas(@PathVariable Long movieId, Model model) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        List<Schedule> schedules = scheduleRepository.findByMovieId(movieId);
        model.addAttribute("movie", movie);
        model.addAttribute("schedules", schedules);
        return "cinemas";
    }

    /** 选座页 */
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

    /** 订单详情 / 支付页 */
    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).orElse(null));
        return "movie-order";
    }

    /** 我的订单 / 票券列表页（新增） */
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderRepository.findAllByOrderByIdDesc());
        return "orders";
    }
}