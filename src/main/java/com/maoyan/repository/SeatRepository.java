package com.maoyan.repository;

import com.maoyan.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByScheduleIdOrderByRowNumAscColNumAsc(Long scheduleId);

    Optional<Seat> findByScheduleIdAndRowNumAndColNum(Long scheduleId, Integer rowNum, Integer colNum);

    List<Seat> findByOrderId(Long orderId);
}
