package com.starscreen.service;

import com.starscreen.common.BusinessException;
import com.starscreen.entity.Schedule;
import com.starscreen.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleRepository scheduleRepository;

    // ==================== 查询 ====================
    public Page<Schedule> listPaged(Long movieId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (movieId != null) {
            return scheduleRepository.findByMovieIdOrderByIdDesc(movieId, pageable);
        }
        return scheduleRepository.findAllByOrderByIdDesc(pageable);
    }

    public Schedule getById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    // ==================== 增删改 ====================
    @Transactional
    public Schedule create(Schedule schedule) {
        validateSchedule(schedule);
        schedule = scheduleRepository.save(schedule);
        log.info("新增场次：id={}, movieId={}, cinema={}, hall={}, time={}",
                schedule.getId(), schedule.getMovieId(), schedule.getCinemaName(),
                schedule.getHallName(), schedule.getStartTime());
        return schedule;
    }

    @Transactional
    public Schedule update(Long id, Schedule schedule) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("场次不存在"));

        if (schedule.getMovieId() != null) existing.setMovieId(schedule.getMovieId());
        if (schedule.getCinemaId() != null) existing.setCinemaId(schedule.getCinemaId());
        if (schedule.getCinemaName() != null) existing.setCinemaName(schedule.getCinemaName());
        if (schedule.getHallName() != null) existing.setHallName(schedule.getHallName());
        if (schedule.getStartTime() != null) existing.setStartTime(schedule.getStartTime());
        if (schedule.getEndTime() != null) existing.setEndTime(schedule.getEndTime());
        if (schedule.getLanguage() != null) existing.setLanguage(schedule.getLanguage());
        if (schedule.getPrice() != null) existing.setPrice(schedule.getPrice());
        if (schedule.getDate() != null) existing.setDate(schedule.getDate());

        existing = scheduleRepository.save(existing);
        log.info("更新场次：id={}", id);
        return existing;
    }

    @Transactional
    public void delete(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("场次不存在"));
        scheduleRepository.delete(schedule);
        log.info("删除场次：id={}", id);
    }

    private void validateSchedule(Schedule s) {
        if (s.getMovieId() == null) throw new BusinessException("电影 ID 不能为空");
        if (s.getCinemaName() == null || s.getCinemaName().trim().isEmpty())
            throw new BusinessException("影院名不能为空");
        if (s.getHallName() == null || s.getHallName().trim().isEmpty())
            throw new BusinessException("影厅名不能为空");
        if (s.getStartTime() == null) throw new BusinessException("开始时间不能为空");
        if (s.getDate() == null) throw new BusinessException("放映日期不能为空");
        if (s.getPrice() == null) s.setPrice(35.0);
        if (s.getLanguage() == null) s.setLanguage("国语 2D");
    }
}
