package com.starscreen.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starscreen.dto.SeatVO;
import com.starscreen.entity.Seat;
import com.starscreen.repository.SeatRepository;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public List<SeatVO> listBySchedule(Long scheduleId) {
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
