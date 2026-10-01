package com.starscreen.config;

import com.starscreen.entity.Movie;
import com.starscreen.entity.Schedule;
import com.starscreen.entity.Seat;
import com.starscreen.repository.MovieRepository;
import com.starscreen.repository.ScheduleRepository;
import com.starscreen.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private MovieRepository movieRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SeatRepository seatRepository;

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() > 0) {
            System.out.println("====== 数据库已有数据，跳过初始化 ======");
            return;
        }

        System.out.println("====== 开始初始化数据 ======");

        // ========== 正在热映 ==========
        movieRepository.save(createMovie("空枪", "/images/poster1.jpg", 9.5, null, "2026-09-22", "showing", null,
                "王小明", "张三,李四,王五", 128));
        movieRepository.save(createMovie("八仙！", "/images/poster2.jpg", 9.7, "2DIMAX", "2026-07-18", "showing", null,
                "张艺谋", "何仙姑,吕洞宾,铁拐李,汉钟离", 135));
        movieRepository.save(createMovie("欢迎来龙餐馆", "/images/poster3.jpg", 9.7, "2DIMAX", "2026-09-22", "showing", null,
                "陈可辛", "葛优,黄渤,沈腾", 145));
        movieRepository.save(createMovie("奥德赛", "/images/poster4.jpg", 9.5, "2DIMAX", "2026-09-22", "showing", null,
                "克里斯托弗·诺兰", "马特·达蒙,汤姆·霍兰德,安妮·海瑟薇", 172));
        movieRepository.save(createMovie("复仇者联盟4：终局之战", "/images/poster5.jpg", 9.2, "2DIMAX", "2026-09-22", "showing", null,
                "罗素兄弟", "小罗伯特·唐尼,克里斯·埃文斯,斯嘉丽·约翰逊", 181));
        movieRepository.save(createMovie("鸳鸯楼底", "/images/poster6.jpg", 8.9, "3D", "2026-09-22", "showing", null,
                "杜琪峰", "刘德华,古天乐,张家辉", 118));
        movieRepository.save(createMovie("痴迷", "/images/poster7.jpg", 9.0, null, "2026-09-22", "showing", null,
                "李安", "梁朝伟,汤唯,王力宏", 158));
        movieRepository.save(createMovie("蜘蛛侠：崭新之日", "/images/poster8.jpg", 9.2, "2DIMAX", "2026-09-22", "showing", null,
                "乔·沃茨", "汤姆·赫兰德,赞达亚,雅各布·巴特朗", 142));

        // ========== 即将上映 ==========
        movieRepository.save(createMovie("水东游", "/images/poster9.jpg", null, null, "2026-09-23", "upcoming", 1238,
                "宁浩", "徐峥,黄渤,王宝强", 128));
        movieRepository.save(createMovie("追光者", "/images/poster10.jpg", null, null, "2026-09-23", "upcoming", 345,
                "贾樟柯", "赵涛,廖凡,董子健", 135));
        movieRepository.save(createMovie("明月照他乡", "/images/poster11.jpg", null, null, "2026-09-24", "upcoming", 726,
                "李睿珺", "海清,武仁林,杨光锐", 133));
        movieRepository.save(createMovie("肆条", "/images/poster12.jpg", null, null, "2026-09-24", "upcoming", 639,
                "宁浩", "黄渤,沈腾,徐峥", 116));
        movieRepository.save(createMovie("惊悚的诞生", "/images/poster13.jpg", null, null, "2026-09-24", "upcoming", 345,
                "温子仁", "帕特里克·威尔森,维拉·法米加", 106));
        movieRepository.save(createMovie("复仇者联盟4：终局之战", "/images/poster14.jpg", null, "2DIMAX", "2026-09-25", "upcoming", 2239942,
                "罗素兄弟", "小罗伯特·唐尼,克里斯·埃文斯,斯嘉丽·约翰逊", 181));
        movieRepository.save(createMovie("老江湖", "/images/poster15.jpg", null, null, "2026-09-25", "upcoming", 68734,
                "杜琪峰", "古天乐,刘德华,张家辉", 124));
        movieRepository.save(createMovie("红孩儿火焰山之王", "/images/poster16.jpg", null, null, "2026-09-25", "upcoming", 30942,
                "饺子", "吕艳婷,囧森瑟夫,瀚墨", 110));

        // ========== 场次 ==========
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "1号厅", "10:30", "12:54", "国语 2D", 34.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "2号厅", "13:00", "15:24", "国语 2D", 38.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "1号厅", "15:30", "17:54", "国语 2D", 38.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "3号厅", "18:00", "20:24", "国语 2D", 42.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 1L, "厦门华侨大学店", "1号厅", "20:30", "22:54", "国语 2D", 42.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 2L, "寰映影城 (集美IOI广场激光IMAX店)", "IMAX厅", "11:00", "13:24", "国语 2D", 30.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 2L, "寰映影城 (集美IOI广场激光IMAX店)", "IMAX厅", "14:20", "16:44", "国语 2D", 35.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 2L, "寰映影城 (集美IOI广场激光IMAX店)", "IMAX厅", "19:00", "21:24", "国语 2D", 40.0, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 3L, "幸福蓝海国际影城 (集美世茂广场IMAX店)", "IMAX厅", "15:00", "17:24", "国语 2D", 34.5, "2026-09-22"));
        scheduleRepository.save(createSchedule(2L, 3L, "幸福蓝海国际影城 (集美世茂广场IMAX店)", "IMAX厅", "20:00", "22:24", "国语 2D", 39.0, "2026-09-22"));

        System.out.println("====== 开始为所有电影补充场次数据 ======");
        List<Movie> allMovies = movieRepository.findAll();
        for (Movie movie : allMovies) {
            if (movie.getId() == 2L) continue;
            scheduleRepository.save(createSchedule(movie.getId(), 1L, "厦门华侨大学店", "1号厅", "10:30", "12:54", "国语 2D", 34.0, "2026-09-22"));
            scheduleRepository.save(createSchedule(movie.getId(), 2L, "寰映影城 (集美IOI广场激光IMAX店)", "IMAX厅", "14:20", "16:44", "国语 2D", 35.0, "2026-09-22"));
            scheduleRepository.save(createSchedule(movie.getId(), 3L, "幸福蓝海国际影城 (集美世茂广场IMAX店)", "IMAX厅", "19:00", "21:24", "国语 2D", 40.0, "2026-09-22"));
        }

        System.out.println("====== 开始生成座位数据 ======");
        List<Schedule> allSchedules = scheduleRepository.findAll();
        Random random = new Random(2026);
        int totalSeats = 0;

        for (Schedule schedule : allSchedules) {
            int[] layout = getLayoutByHall(schedule.getHallName());
            int rows = layout[0];
            int cols = layout[1];

            List<Seat> seats = new ArrayList<>();
            for (int r = 1; r <= rows; r++) {
                for (int c = 1; c <= cols; c++) {
                    Seat seat = new Seat();
                    seat.setScheduleId(schedule.getId());
                    seat.setRowNum(r);
                    seat.setColNum(c);
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

    private int[] getLayoutByHall(String hallName) {
        if (hallName == null) return new int[]{6, 10};
        if (hallName.contains("IMAX")) return new int[]{10, 16};
        if (hallName.contains("3号厅")) return new int[]{5, 8};
        if (hallName.contains("2号厅")) return new int[]{7, 12};
        return new int[]{6, 10};
    }

    private Movie createMovie(String title, String poster, Double score, String tag,
                              String releaseDate, String status, Integer wantCount,
                              String director, String actors, Integer duration) {
        Movie m = new Movie();
        m.setTitle(title);
        m.setPoster(poster);
        m.setScore(score);
        m.setTag(tag);
        m.setReleaseDate(releaseDate);
        m.setStatus(status);
        m.setWantCount(wantCount);
        m.setDirector(director);
        m.setActors(actors);
        m.setDuration(duration);
        return m;
    }

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