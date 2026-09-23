package com.maoyan.controller;

import com.maoyan.dto.SeatVO;
import com.maoyan.entity.Seat;
import com.maoyan.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    @GetMapping("/schedule/{scheduleId}")
    public List<SeatVO> listBySchedule(@PathVariable Long scheduleId) {
        List<Seat> seats = seatRepository
                .findByScheduleIdOrderByRowNumAscColNumAsc(scheduleId);

        List<SeatVO> result = new ArrayList<>(seats.size());
        for (Seat seat : seats) {
            SeatVO vo = new SeatVO();
            vo.setId(seat.getId());
            vo.setRowNum(seat.getRowNum());
            vo.setColNum(seat.getColNum());
            vo.setStatus(seat.getStatus());
            vo.setLabel(seat.getRowNum() + "排" + seat.getColNum() + "座");
            result.add(vo);
        }
        return result;
    }
}
