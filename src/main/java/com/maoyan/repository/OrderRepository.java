package com.maoyan.repository;

import com.maoyan.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(String status);

    List<Order> findAllByOrderByIdDesc();

    List<Order> findByStatusOrderByIdDesc(String status);

    /** 统计某状态的订单数 */
    long countByStatus(String status);

    /** 统计已支付订单的总票房 */
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'paid'")
    Double sumPaidTotalPrice();

    /** 按电影统计票房（TOP N） */
    @Query("SELECT o.movieTitle, SUM(o.totalPrice), COUNT(o) " +
           "FROM Order o WHERE o.status = 'paid' " +
           "GROUP BY o.movieTitle " +
           "ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> topMoviesByRevenue();
}
