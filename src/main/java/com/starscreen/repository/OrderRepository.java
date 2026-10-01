package com.starscreen.repository;

import com.starscreen.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /** 按状态查询（全部） */
    List<Order> findByStatus(String status);

    /** 全部订单，最新在前 */
    List<Order> findAllByOrderByIdDesc();

    /** 按状态查询，最新在前 */
    List<Order> findByStatusOrderByIdDesc(String status);

    /** 分页：全部订单（新增） */
    Page<Order> findAllByOrderByIdDesc(Pageable pageable);

    /** 分页：按状态查询（新增） */
    Page<Order> findByStatusOrderByIdDesc(String status, Pageable pageable);

    /** 统计某状态的订单数 */
    long countByStatus(String status);

    /** 统计已支付订单的总票房 */
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'paid'")
    Double sumPaidTotalPrice();

    /** 按电影统计票房 */
    @Query("SELECT o.movieTitle, SUM(o.totalPrice), COUNT(o) " +
           "FROM Order o WHERE o.status = 'paid' " +
           "GROUP BY o.movieTitle " +
           "ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> topMoviesByRevenue();
}
