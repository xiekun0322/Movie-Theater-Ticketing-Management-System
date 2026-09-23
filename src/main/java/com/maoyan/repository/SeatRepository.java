package com.maoyan.repository;

import com.maoyan.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    /** 按场次查询座位（按排、列排序） */
    List<Seat> findByScheduleIdOrderByRowNumAscColNumAsc(Long scheduleId);

    /** 按场次 + 排 + 列查具体座位 */
    Optional<Seat> findByScheduleIdAndRowNumAndColNum(Long scheduleId, Integer rowNum, Integer colNum);

    /** 按订单 ID 查座位（新增） */
    List<Seat> findByOrderId(Long orderId);
}