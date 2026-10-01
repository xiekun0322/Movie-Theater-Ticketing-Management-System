package com.starscreen.controller;

import com.starscreen.common.Result;
import com.starscreen.dto.CreateOrderRequest;
import com.starscreen.entity.Order;
import com.starscreen.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    /** 我的订单列表 */
    @GetMapping
    public Result<java.util.List<Order>> list(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return Result.error(401, "请先登录");
        return Result.success(orderService.listPagedByUser(userId, "all", 0, 1000).getContent());
    }

    /** 订单详情 */
    @GetMapping("/{id}")
    public Result<Order> detail(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Order order = orderService.getById(id);
        if (order == null) return Result.error(404, "订单不存在");
        if (userId != null && !userId.equals(order.getUserId())) {
            return Result.error(403, "无权查看该订单");
        }
        return Result.success(order);
    }

    /** 创建订单 */
    @PostMapping
    public Result<Order> create(@RequestBody @Valid CreateOrderRequest req, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return Result.error(401, "请先登录");
        return Result.success(orderService.createOrder(userId, req.getScheduleId(), req.getSeats()));
    }

    /** 支付 */
    @PostMapping("/{id}/pay")
    public Result<Order> pay(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return Result.error(401, "请先登录");
        return Result.success(orderService.pay(userId, id));
    }

    /** 取消 */
    @PostMapping("/{id}/cancel")
    public Result<Order> cancel(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return Result.error(401, "请先登录");
        return Result.success(orderService.cancel(userId, id));
    }
}