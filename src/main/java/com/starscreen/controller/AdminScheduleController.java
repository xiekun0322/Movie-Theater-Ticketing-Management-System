package com.starscreen.controller;

import com.starscreen.common.Result;
import com.starscreen.entity.Schedule;
import com.starscreen.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/schedules")
@CrossOrigin
public class AdminScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public Result<Page<Schedule>> list(@RequestParam(required = false) Long movieId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return Result.success(scheduleService.listPaged(movieId, page, size));
    }

    @GetMapping("/{id}")
    public Result<Schedule> detail(@PathVariable Long id) {
        return Result.success(scheduleService.getById(id));
    }

    @PostMapping
    public Result<Schedule> create(@RequestBody Schedule schedule) {
        return Result.success(scheduleService.create(schedule));
    }

    @PutMapping("/{id}")
    public Result<Schedule> update(@PathVariable Long id, @RequestBody Schedule schedule) {
        return Result.success(scheduleService.update(id, schedule));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return Result.success();
    }
}