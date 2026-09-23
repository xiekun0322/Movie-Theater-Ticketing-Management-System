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

    @GetMapping
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order detail(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateOrderRequest req) {
        try {
            Order order = orderService.createOrder(req.getScheduleId(), req.getSeats());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> pay(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.pay(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.cancel(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
