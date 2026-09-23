package com.maoyan.config; // 声明当前类所在的包

import com.maoyan.entity.Movie; // 导入 Movie 实体
import com.maoyan.entity.Schedule; // 导入 Schedule 实体
import com.maoyan.entity.Seat; // 导入 Seat 实体
import com.maoyan.repository.MovieRepository; // 导入电影仓库
import com.maoyan.repository.ScheduleRepository; // 导入场次仓库
import com.maoyan.repository.SeatRepository; // 导入座位仓库
import org.springframework.beans.factory.annotation.Autowired; // 导入 @Autowired
import org.springframework.boot.CommandLineRunner; // 导入启动运行接口
import org.springframework.stereotype.Component; // 导入 @Component

import java.util.ArrayList; // 导入 ArrayList
import java.util.List; // 导入 List
import java.util.Random; // 导入 Random

/**
 * 数据初始化器
 * 项目启动时自动插入测试数据（电影、场次、座位）
 */
@Component // 声明为 Spring 组件
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository; // 电影仓库

    @Autowired
    private ScheduleRepository scheduleRepository; // 场次仓库

    @Autowired
    private SeatRepository seatRepository; // 座位仓库

    /** 座位图配置：5 排 × 12 列 */
    private static final int SEAT_ROWS = 5;
    private static final int SEAT_COLS = 12;

    /**
     * 启动时自动执行，插入测试数据
     */
    @Override
    public void run(String... args) throws Exception {
        // 已有数据则跳过
        if (movieRepository.count() > 0) {
            System.out.println("====== 数据库已有数据，跳过初始化 ======");
            return;
        }

        System.out.println("====== 开始初始化数据 ======");

        // ========== 插入电影 ==========
        // 正在热映（id 1~8）
        movieRepository.save(createMovie("空枪", "linear-gradient(45deg, #1a2a6c, #b21f1f)", 9.5, null, "2026-09-22", "showing", null));
        movieRepository.save(createMovie("八仙！", "linear-gradient(45deg, #00b4db, #0083b0)", 9.7, "2DIMAX", "2026-07-18", "showing", null));
        movieRepository.save(createMovie("欢迎来龙餐馆", "linear-gradient(45deg, #f2994a, #f2c94c)", 9.7, "2DIMAX", "2026-09-22", "showing", null));
        movieRepository.save(createMovie("奥德赛", "linear-gradient(45deg, #141e30, #243b55)", 9.5, "2DIMAX", "2026-09-22", "showing", null));
        movieRepository.save(createMovie("复仇者联盟4：终局之战", "linear-gradient(45deg, #4b6cb7, #182848)", 9.2, "2DIMAX", "2026-09-22", "showing", null));
        movieRepository.save(createMovie("鸳鸯楼底", "linear-gradient(45deg, #cb2d3e, #ef473a)", 8.9, "3D", "2026-09-22", "showing", null));
        movieRepository.save(createMovie("痴迷", "linear-gradient(45deg, #000000, #434343)", 9.0, null, "2026-09-22", "showing", null));
        movieRepository.save(createMovie("蜘蛛侠：崭新之日", "linear-gradient(45deg, #ed213a, #93291e)", 9.2, "2DIMAX", "2026-09-22", "showing", null));

        // 即将上映（id 9~16）
        movieRepository.save(createMovie("水东游", "linear-gradient(45deg, #f12711, #f5af19)", null, null, "2026-09-23", "upcoming", 1238));
        movieRepository.save(createMovie("追光者", "linear-gradient(45deg, #200122, #6f0000)", null, null, "2026-09-23", "upcoming", 345));
        movieRepository.save(createMovie("明月照他乡", "linear-gradient(45deg, #2193b0, #6dd5ed)", null, null, "2026-09-24", "upcoming", 726));
        movieRepository.save(createMovie("肆条", "linear-gradient(45deg, #11998e, #38ef7d)", null, null, "2026-09-24", "upcoming", 639));
        movieRepository.save(createMovie("惊悚的诞生", "linear-gradient(45deg, #333333, #dd1818)", null, null, "2026-09-24", "upcoming", 345));
        movieRepository.save(createMovie("复仇者联盟4：终局之战", "linear-gradient(45deg, #000046, #1cb5e0)", null, "2DIMAX", "2026-09-25", "upcoming", 2239942));
        movieRepository.save(createMovie("老江湖", "linear-gradient(45deg, #870000, #190a05)", null, null, "2026-09-25", "upcoming", 68734));
        movieRepository.save(createMovie("红孩儿火焰山之王", "linear-gradient(45deg, #f12711, #f5af19)", null, null, "2026-09-25", "upcoming", 30942));

        // ========== 插入场次（电影 ID=2 八仙！） ==========
        // 影院 1：厦门华侨大学店
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "1号厅", "10:30", "12:54", "国语 2D", 34.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "2号厅", "13:00", "15:24", "国语 2D", 38.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "1号厅", "15:30", "17:54", "国语 2D", 38.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "3号厅", "18:00", "20:24", "国语 2D", 42.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "1号厅", "20:30", "22:54", "国语 2D", 42.0, "2026-09-22"));

        // 影院 2：寰映影城
        scheduleRepository.save(createSchedule(2L, 2L, "寰映影城 (集美IOI广场激光IMAX店)", "IMAX厅", "11:00", "13:24", "国语 2D", 30.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 2L, "寰映影城 (集美IOI广场激光IMAX店)", "IMAX厅", "14:20", "16:44", "国语 2D", 35.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 2L, "寰映影城 (集美IOI广场激光IMAX店)", "IMAX厅", "19:00", "21:24", "国语 2D", 40.0, "2026-09-22"));

        // 影院 3：幸福蓝海国际影城
        scheduleRepository.save(createSchedule(2L, 3L, "幸福蓝海国际影城 (集美世茂广场IMAX店)", "IMAX厅", "15:00", "17:24", "国语 2D", 34.5, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 3L, "幸福蓝海国际影城 (集美世茂广场IMAX店)", "IMAX厅", "20:00", "22:24", "国语 2D", 39.0, "2026-09-22"));

        // ========== 给所有其他电影补充场次 ==========
        System.out.println("====== 开始为所有电影补充场次数据 ======");
        List<Movie> allMovies = movieRepository.findAll();
        for (Movie movie : allMovies) {
            // 跳过《八仙！》，它已经有自己的场次了
            if (movie.getId() == 2L) continue;

            // 为每部电影插入 3 个不同影院的场次
            scheduleRepository.save(createSchedule(movie.getId(), 1L, "厦门华侨大学店", "1号厅", "10:30", "12:54", "国语 2D", 34.0, "2026-09-22"));
            scheduleRepository.save(createSchedule(movie.getId(), 2L, "寰映影城 (集美IOI广场激光IMAX店)", "IMAX厅", "14:20", "16:44", "国语 2D", 35.0, "2026-09-22"));
            scheduleRepository.save(createSchedule(movie.getId(), 3L, "幸福蓝海国际影城 (集美世茂广场IMAX店)", "IMAX厅", "19:00", "21:24", "国语 2D", 40.0, "2026-09-22"));
        }

        // ========== 为所有场次生成座位 ==========
        System.out.println("====== 开始生成座位数据 ======");
        List<Schedule> allSchedules = scheduleRepository.findAll();
        Random random = new Random(2026); // 固定种子，保证结果可复现
        int totalSeats = 0;

        for (Schedule schedule : allSchedules) {
            List<Seat> seats = new ArrayList<>();
            for (int r = 1; r <= SEAT_ROWS; r++) {
                for (int c = 1; c <= SEAT_COLS; c++) {
                    Seat seat = new Seat();
                    seat.setScheduleId(schedule.getId());
                    seat.setRowNum(r);
                    seat.setColNum(c);
                    // 约 8% 座位随机预置为已售，模拟真实上座率
                    seat.setStatus(random.nextInt(100) < 8 ? "sold" : "available");
                    seats.add(seat);
                }
            }
            seatRepository.saveAll(seats);
            totalSeats += seats.size();
        }
        System.out.println("====== 共生成座位 " + totalSeats + " 个 ======");

        System.out.println("====== 数据初始化完成！ ======");
    }

    /** 辅助方法：创建电影对象 */
    private Movie createMovie(String title, String poster, Double score, String tag,
                              String releaseDate, String status, Integer wantCount) {
        Movie m = new Movie();
        m.setTitle(title);
        m.setPoster(poster);
        m.setScore(score);
        m.setTag(tag);
        m.setReleaseDate(releaseDate);
        m.setStatus(status);
        m.setWantCount(wantCount);
        return m;
    }

    /** 辅助方法：创建场次对象 */
    private Schedule createSchedule(Long movieId, Long cinemaId, String cinemaName,
                                    String hallName, String startTime, String endTime,
                                    String language, Double price, String date) {
        Schedule s = new Schedule();
        s.setMovieId(movieId);
        s.setCinemaId(cinemaId);
        s.setCinemaName(cinemaName);
        s.setHallName(hallName);
        s.setStartTime(startTime);
        s.setEndTime(endTime);
        s.setLanguage(language);
        s.setPrice(price);
        s.setDate(date);
        return s;
    }
}