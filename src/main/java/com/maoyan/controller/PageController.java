package com.maoyan.controller; // 声明当前类所在的包

import com.maoyan.entity.Movie; // 导入 Movie 实体
import com.maoyan.entity.Schedule; // 导入 Schedule 实体
import com.maoyan.repository.MovieRepository; // 导入电影仓库
import com.maoyan.repository.ScheduleRepository; // 导入场次仓库
import org.springframework.beans.factory.annotation.Autowired; // 导入 @Autowired
import org.springframework.stereotype.Controller; // 导入 @Controller
import org.springframework.ui.Model; // 导入 Model，用于向页面传递数据
import org.springframework.web.bind.annotation.GetMapping; // 导入 @GetMapping
import org.springframework.web.bind.annotation.PathVariable; // 导入 @PathVariable
import org.springframework.web.bind.annotation.ResponseBody; // 导入 @ResponseBody

import java.util.List; // 导入 List

/**
 * 页面跳转控制器
 * 负责所有页面的路由跳转，返回视图名，由 Thymeleaf 渲染 HTML
 */
@Controller // 页面控制器，返回视图名
public class PageController {

    @Autowired // 注入电影仓库
    private MovieRepository movieRepository;

    @Autowired // 注入场次仓库
    private ScheduleRepository scheduleRepository;

    /**
     * 诊断接口：验证 PageController 是否被 Spring 扫描到
     * 访问路径：GET /test
     * 返回纯文本（不走模板引擎）
     */
    @GetMapping("/test")
    @ResponseBody // 直接把返回值作为 HTTP 响应体输出，不渲染模板
    public String test() {
        return "PageController 工作正常";
    }

    /**
     * 首页
     * 访问路径：GET /
     * 返回视图：templates/index.html
     */
    @GetMapping("/")
    public String index(Model model) {
        // 查询正在热映的电影
        List<Movie> showingMovies = movieRepository.findByStatus("showing");
        // 查询即将上映的电影
        List<Movie> upcomingMovies = movieRepository.findByStatus("upcoming");

        // 把数据放到 Model 中，Thymeleaf 通过 ${showingMovies} 获取
        model.addAttribute("showingMovies", showingMovies);
        model.addAttribute("upcomingMovies", upcomingMovies);

        return "index"; // 返回视图名，对应 templates/index.html
    }

    /**
     * 电影详情页
     * 访问路径：GET /movie/detail/{id}
     */
    @GetMapping("/movie/detail/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        List<Schedule> schedules = scheduleRepository.findByMovieId(id);
        model.addAttribute("movie", movie);
        model.addAttribute("schedules", schedules);
        return "movie-detail";
    }

    /**
     * 影院列表页
     * 访问路径：GET /cinema
     */
    @GetMapping("/cinema")
    public String cinema() {
        return "cinema";
    }

    /**
     * 选座页
     * 访问路径：GET /seat/{scheduleId}
     */
    @GetMapping("/seat/{scheduleId}")
    public String seat(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        model.addAttribute("schedule", schedule);
        return "seat";
    }
}
