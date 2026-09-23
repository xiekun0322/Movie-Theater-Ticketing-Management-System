package com.maoyan.repository;

import com.maoyan.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /** 按状态查询订单（用于扫描超时订单） */
    List<Order> findByStatus(String status);

    /** 全部订单，最新的在前 */
    List<Order> findAllByOrderByIdDesc();
}