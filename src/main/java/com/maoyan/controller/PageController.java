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
 * 负责所有页面的路由跳转，返回视图名，由 Thymeleaf 渲染 HTML
 */
@Controller // 页面控制器（不是 @RestController）
public class PageController {

    @Autowired
    private MovieRepository movieRepository; // 电影仓库

    @Autowired
    private ScheduleRepository scheduleRepository; // 场次仓库

    @Autowired
    private OrderRepository orderRepository; // 订单仓库

    /** 诊断接口：验证 Controller 是否被扫描 */
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "PageController 工作正常";
    }

    /**
     * 首页
     * 访问：GET /
     * 返回：templates/index.html
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Movie> showingMovies = movieRepository.findByStatus("showing");
        List<Movie> upcomingMovies = movieRepository.findByStatus("upcoming");
        model.addAttribute("showingMovies", showingMovies);
        model.addAttribute("upcomingMovies", upcomingMovies);
        return "index";
    }

    /**
     * 电影详情页
     * 访问：GET /movie/detail/{id}
     * 返回：templates/movie-detail.html
     */
    @GetMapping("/movie/detail/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        model.addAttribute("movie", movie);
        return "movie-detail";
    }

    /**
     * 选影院/选场次页
     * 访问：GET /cinemas/{movieId}
     * 返回：templates/cinemas.html
     */
    @GetMapping("/cinemas/{movieId}")
    public String cinemas(@PathVariable Long movieId, Model model) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        List<Schedule> schedules = scheduleRepository.findByMovieId(movieId);
        model.addAttribute("movie", movie);
        model.addAttribute("schedules", schedules);
        return "cinemas";
    }

    /**
     * 选座页
     * 访问：GET /seat/{scheduleId}
     * 返回：templates/seat.html   ← 必须是 "seat"
     */
    @GetMapping("/seat/{scheduleId}")
    public String seat(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        model.addAttribute("schedule", schedule);

        if (schedule != null) {
            Movie movie = movieRepository.findById(schedule.getMovieId()).orElse(null);
            model.addAttribute("movie", movie);
        }
        return "seat";   // ← 必须是 "seat"
    }

    /**
     * 订单详情 / 支付页
     * 访问：GET /order/{id}
     * 返回：templates/order-detail.html   ← 必须是 "order-detail"
     */
    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).orElse(null));
        return "order-detail";   // ← 必须是 "order-detail"
    }
}
