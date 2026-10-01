package com.starscreen.service;

import com.starscreen.dto.AdminStats;
import com.starscreen.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private OrderRepository orderRepository;

    public AdminStats getStats() {
        AdminStats stats = new AdminStats();

        stats.setTotalOrders(orderRepository.count());
        stats.setPendingOrders(orderRepository.countByStatus("pending"));
        stats.setPaidOrders(orderRepository.countByStatus("paid"));
        stats.setCancelledOrders(orderRepository.countByStatus("cancelled"));
        stats.setTotalRevenue(orderRepository.sumPaidTotalPrice());
        stats.setTopMovies(orderRepository.topMoviesByRevenue());
        stats.setDailyRevenue(orderRepository.dailyRevenue());   // 新增

        return stats;
    }
}
