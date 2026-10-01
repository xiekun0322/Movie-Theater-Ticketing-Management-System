package com.starscreen.repository;

import com.starscreen.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ========== 无用户过滤（旧方法，AdminController 等可能用） ==========
    List<Order> findByStatus(String status);
    List<Order> findAllByOrderByIdDesc();
    List<Order> findByStatusOrderByIdDesc(String status);
    Page<Order> findAllByOrderByIdDesc(Pageable pageable);
    Page<Order> findByStatusOrderByIdDesc(String status, Pageable pageable);

    // ========== 按 userId 过滤（新增） ==========
    Page<Order> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);
    Page<Order> findByUserIdAndStatusOrderByIdDesc(Long userId, String status, Pageable pageable);
    List<Order> findByStatusAndId(String status, Long id);

    // ========== 统计 ==========
    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'paid'")
    Double sumPaidTotalPrice();

    @Query("SELECT o.movieTitle, SUM(o.totalPrice), COUNT(o) " +
           "FROM Order o WHERE o.status = 'paid' " +
           "GROUP BY o.movieTitle " +
           "ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> topMoviesByRevenue();
}