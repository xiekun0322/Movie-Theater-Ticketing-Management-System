package com.maoyan.controller;

import com.maoyan.common.Result;
import com.maoyan.dto.CreateOrderRequest;
import com.maoyan.entity.Order;
import com.maoyan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    /** 全部订单 */
    @GetMapping
    public Result<List<Order>> list() {
        return Result.success(orderService.listAll());
    }

    /** 订单详情 */
    @GetMapping("/{id}")
    public Result<Order> detail(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    /** 锁座 + 创建待支付订单 */
    @PostMapping
    public Result<Order> create(@RequestBody CreateOrderRequest req) {
        return Result.success(orderService.createOrder(req.getScheduleId(), req.getSeats()));
    }

    /** 模拟支付 */
    @PostMapping("/{id}/pay")
    public Result<Order> pay(@PathVariable Long id) {
        return Result.success(orderService.pay(id));
    }

    /** 取消订单 */
    @PostMapping("/{id}/cancel")
    public Result<Order> cancel(@PathVariable Long id) {
        return Result.success(orderService.cancel(id));
    }
}
