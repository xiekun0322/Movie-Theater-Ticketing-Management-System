package com.maoyan.service;

import com.maoyan.entity.Movie;
import com.maoyan.entity.Order;
import com.maoyan.entity.Schedule;
import com.maoyan.entity.Seat;
import com.maoyan.repository.MovieRepository;
import com.maoyan.repository.OrderRepository;
import com.maoyan.repository.ScheduleRepository;
import com.maoyan.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OrderService {

    private static final int PAY_TIMEOUT_MINUTES = 10;

    private static final DateTimeFormatter DTF =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Pattern SEAT_LABEL = Pattern.compile("^(\\d+)排(\\d+)座$");
    private static final int MAX_SEATS_PER_ORDER = 6;

    @Autowired private OrderRepository orderRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private MovieRepository movieRepository;

    @Transactional
    public Order createOrder(Long scheduleId, List<String> seatLabels) {
        if (scheduleId == null) {
            throw new RuntimeException("场次 ID 不能为空");
        }
        if (seatLabels == null || seatLabels.isEmpty()) {
            throw new RuntimeException("请至少选择一个座位");
        }
        if (seatLabels.size() > MAX_SEATS_PER_ORDER) {
            throw new RuntimeException("一次最多选择 " + MAX_SEATS_PER_ORDER + " 个座位");
        }

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("场次不存在"));

        List<Seat> seats = new ArrayList<>();
        for (String label : seatLabels) {
            int[] rc = parseSeatLabel(label);
            Seat seat = seatRepository
                    .findByScheduleIdAndRowNumAndColNum(scheduleId, rc[0], rc[1])
                    .orElseThrow(() -> new RuntimeException("座位不存在：" + label));
            if (!"available".equals(seat.getStatus())) {
                throw new RuntimeException("座位已被选走：" + label);
            }
            seats.add(seat);
        }

        Movie movie = movieRepository.findById(schedule.getMovieId()).orElse(null);

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setScheduleId(scheduleId);
        order.setMovieId(schedule.getMovieId());
        order.setMovieTitle(movie != null ? movie.getTitle() : "未知电影");
        order.setCinemaName(schedule.getCinemaName());
        order.setHallName(schedule.getHallName());
        order.setShowTime(schedule.getDate() + " " + schedule.getStartTime());
        order.setSeats(String.join(",", seatLabels));
        order.setTotalPrice(schedule.getPrice() * seatLabels.size());
        order.setStatus("pending");
        order.setCreateTime(LocalDateTime.now().format(DTF));
        order = orderRepository.save(order);

        for (Seat seat : seats) {
            seat.setStatus("locked");
            seat.setOrderId(order.getId());
        }
        seatRepository.saveAll(seats);

        return order;
    }

    @Transactional
    public Order pay(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if ("paid".equals(order.getStatus())) {
            throw new RuntimeException("订单已支付，请勿重复操作");
        }
        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许支付：" + order.getStatus());
        }

        order.setStatus("paid");
        order.setPayTime(LocalDateTime.now().format(DTF));
        order.setTicketCode(generateTicketCode());
        orderRepository.save(order);

        List<Seat> seats = seatRepository.findByOrderId(order.getId());
        for (Seat seat : seats) {
            seat.setStatus("sold");
        }
        seatRepository.saveAll(seats);

        return order;
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if ("paid".equals(order.getStatus())) {
            throw new RuntimeException("已支付订单不可取消");
        }
        if ("cancelled".equals(order.getStatus())) {
            return order;
        }

        order.setStatus("cancelled");
        orderRepository.save(order);
        releaseSeats(order.getId());
        return order;
    }

    @Scheduled(fixedRate = 30_000)
    @Transactional
    public void cancelExpiredOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus("pending");
        if (pendingOrders.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();
        int cancelledCount = 0;

        for (Order order : pendingOrders) {
            if (order.getCreateTime() == null) continue;

            LocalDateTime created;
            try {
                created = LocalDateTime.parse(order.getCreateTime(), DTF);
            } catch (Exception e) {
                continue;
            }

            if (created.plusMinutes(PAY_TIMEOUT_MINUTES).isBefore(now)) {
                order.setStatus("cancelled");
                orderRepository.save(order);
                releaseSeats(order.getId());
                cancelledCount++;
            }
        }

        if (cancelledCount > 0) {
            System.out.println("====== 自动取消超时订单 " + cancelledCount + " 个 ======");
        }
    }

    private void releaseSeats(Long orderId) {
        List<Seat> seats = seatRepository.findByOrderId(orderId);
        for (Seat seat : seats) {
            seat.setStatus("available");
            seat.setOrderId(null);
        }
        seatRepository.saveAll(seats);
    }

    private int[] parseSeatLabel(String label) {
        Matcher m = SEAT_LABEL.matcher(label == null ? "" : label.trim());
        if (!m.matches()) {
            throw new RuntimeException("座位格式错误：" + label);
        }
        return new int[]{Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))};
    }

    private String generateOrderNo() {
        return "MO" + System.currentTimeMillis()
                + String.format("%03d", new Random().nextInt(1000));
    }

    private String generateTicketCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i == 4) sb.append('-');
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
