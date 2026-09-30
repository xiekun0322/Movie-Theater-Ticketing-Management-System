package com.starscreen.controller;

import com.starscreen.common.Result;
import com.starscreen.dto.CreateOrderRequest;
import com.starscreen.entity.Order;
import com.starscreen.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    /** 全部订单 / 按状态筛选（新增 status 参数） */
    @GetMapping
    public Result<List<Order>> list(@RequestParam(required = false) String status) {
        return Result.success(orderService.listByStatus(status));
    }

    /** 订单详情 */
    @GetMapping("/{id}")
    public Result<Order> detail(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    /** 锁座 + 创建待支付订单 */
    @PostMapping
    public Result<Order> create(@RequestBody @Valid CreateOrderRequest req) {
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