package com.starscreen.repository;

import com.starscreen.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 基础查询
    List<Order> findByStatus(String status);
    List<Order> findAllByOrderByIdDesc();
    List<Order> findByStatusOrderByIdDesc(String status);
    Page<Order> findAllByOrderByIdDesc(Pageable pageable);
    Page<Order> findByStatusOrderByIdDesc(String status, Pageable pageable);

    // 用户隔离
    Page<Order> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);
    Page<Order> findByUserIdAndStatusOrderByIdDesc(Long userId, String status, Pageable pageable);

    // 统计
    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'paid'")
    Double sumPaidTotalPrice();

    @Query("SELECT o.movieTitle, SUM(o.totalPrice), COUNT(o) " +
           "FROM Order o WHERE o.status = 'paid' " +
           "GROUP BY o.movieTitle " +
           "ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> topMoviesByRevenue();

    /** 按天统计票房（pay_time 前 10 位是日期） */
    @Query("SELECT SUBSTRING(o.payTime, 1, 10), SUM(o.totalPrice), COUNT(o) " +
           "FROM Order o WHERE o.status = 'paid' AND o.payTime IS NOT NULL " +
           "GROUP BY SUBSTRING(o.payTime, 1, 10) " +
           "ORDER BY SUBSTRING(o.payTime, 1, 10) ASC")
    List<Object[]> dailyRevenue();
}
