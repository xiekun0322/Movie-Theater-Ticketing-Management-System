package com.maoyan.controller;

import com.maoyan.common.Result;
import com.maoyan.dto.SeatVO;
import com.maoyan.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/schedule/{scheduleId}")
    public Result<List<SeatVO>> listBySchedule(@PathVariable Long scheduleId) {
        return Result.success(seatService.listBySchedule(scheduleId));
    }
}
