package com.maoyan.controller;

import com.maoyan.dto.CreateOrderRequest;
import com.maoyan.entity.Order;
import com.maoyan.repository.OrderRepository;
import com.maoyan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单接口：创建 / 支付 / 取消 / 查询
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;

    /** 全部订单 */
    @GetMapping
    public List<Order> list() {
        return orderRepository.findAll();
    }

    /** 订单详情 */
    @GetMapping("/{id}")
    public Order detail(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    /** 锁座 + 创建待支付订单 */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateOrderRequest req) {
        try {
            Order order = orderService.createOrder(req.getScheduleId(), req.getSeats());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /** 模拟支付（新增） */
    @PostMapping("/{id}/pay")
    public ResponseEntity<?> pay(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.pay(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /** 取消订单（新增） */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.cancel(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}