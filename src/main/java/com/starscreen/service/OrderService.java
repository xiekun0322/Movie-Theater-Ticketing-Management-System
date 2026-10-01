package com.starscreen.service;

import com.starscreen.common.BusinessException;
import com.starscreen.entity.Movie;
import com.starscreen.entity.Order;
import com.starscreen.entity.Schedule;
import com.starscreen.entity.Seat;
import com.starscreen.repository.MovieRepository;
import com.starscreen.repository.OrderRepository;
import com.starscreen.repository.ScheduleRepository;
import com.starscreen.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final int PAY_TIMEOUT_MINUTES = 10;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Pattern SEAT_LABEL = Pattern.compile("^(\\d+)排(\\d+)座$");
    private static final int MAX_SEATS_PER_ORDER = 6;

    @Autowired private OrderRepository orderRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private MovieRepository movieRepository;

    // ==================== 1. 锁座下单 ====================
    @Transactional
    public Order createOrder(Long userId, Long scheduleId, List<String> seatLabels) {
        if (userId == null) throw new BusinessException("请先登录");
        if (scheduleId == null) throw new BusinessException("场次 ID 不能为空");
        if (seatLabels == null || seatLabels.isEmpty()) throw new BusinessException("请至少选择一个座位");
        if (seatLabels.size() > MAX_SEATS_PER_ORDER)
            throw new BusinessException("一次最多选择 " + MAX_SEATS_PER_ORDER + " 个座位");

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException("场次不存在"));

        List<Seat> seats = new ArrayList<>();
        for (String label : seatLabels) {
            int[] rc = parseSeatLabel(label);
            Seat seat = seatRepository
                    .findByScheduleIdAndRowNumAndColNum(scheduleId, rc[0], rc[1])
                    .orElseThrow(() -> new BusinessException("座位不存在：" + label));
            if (!"available".equals(seat.getStatus()))
                throw new BusinessException("座位已被选走：" + label);
            seats.add(seat);
        }

        Movie movie = movieRepository.findById(schedule.getMovieId()).orElse(null);

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
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

        log.info("创建订单成功：orderNo={}, userId={}, scheduleId={}, seats={}, totalPrice={}",
                order.getOrderNo(), userId, scheduleId, seatLabels, order.getTotalPrice());
        return order;
    }

    // ==================== 2. 支付（校验用户） ====================
    @Transactional
    public Order pay(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (userId != null && !userId.equals(order.getUserId())) {
            throw new BusinessException("无权操作该订单");
        }

        if ("paid".equals(order.getStatus())) {
            log.warn("重复支付被拒绝：orderId={}", orderId);
            throw new BusinessException("订单已支付，请勿重复操作");
        }
        if (!"pending".equals(order.getStatus())) {
            log.warn("非法状态支付被拒绝：orderId={}, status={}", orderId, order.getStatus());
            throw new BusinessException("订单状态不允许支付：" + order.getStatus());
        }

        order.setStatus("paid");
        order.setPayTime(LocalDateTime.now().format(DTF));
        order.setTicketCode(generateTicketCode());
        orderRepository.save(order);

        List<Seat> seats = seatRepository.findByOrderId(order.getId());
        for (Seat seat : seats) seat.setStatus("sold");
        seatRepository.saveAll(seats);

        log.info("订单支付成功：orderId={}, orderNo={}, ticketCode={}, totalPrice={}",
                orderId, order.getOrderNo(), order.getTicketCode(), order.getTotalPrice());
        return order;
    }

    // ==================== 3. 取消订单（校验用户） ====================
    @Transactional
    public Order cancel(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (userId != null && !userId.equals(order.getUserId())) {
            throw new BusinessException("无权操作该订单");
        }

        if ("paid".equals(order.getStatus())) throw new BusinessException("已支付订单不可取消");
        if ("cancelled".equals(order.getStatus())) return order;

        order.setStatus("cancelled");
        orderRepository.save(order);
        releaseSeats(order.getId());

        log.info("手动取消订单：orderId={}, orderNo={}", orderId, order.getOrderNo());
        return order;
    }

    // ==================== 4. 定时取消超时订单（系统任务，不校验用户） ====================
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
                log.warn("订单时间格式异常，跳过：orderId={}", order.getId());
                continue;
            }

            if (created.plusMinutes(PAY_TIMEOUT_MINUTES).isBefore(now)) {
                order.setStatus("cancelled");
                orderRepository.save(order);
                releaseSeats(order.getId());
                cancelledCount++;
                log.info("超时取消订单：orderId={}, orderNo={}", order.getId(), order.getOrderNo());
            }
        }
        if (cancelledCount > 0) log.info("本轮共自动取消超时订单 {} 个", cancelledCount);
    }

    // ==================== 查询 ====================
    /** 分页查询（按用户过滤） */
    public Page<Order> listPagedByUser(Long userId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (status == null || status.isEmpty() || "all".equals(status)) {
            return orderRepository.findByUserIdOrderByIdDesc(userId, pageable);
        }
        return orderRepository.findByUserIdAndStatusOrderByIdDesc(userId, status, pageable);
    }

    /** 后台统计用（全部订单） */
    public Page<Order> listPaged(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (status == null || status.isEmpty() || "all".equals(status)) {
            return orderRepository.findAllByOrderByIdDesc(pageable);
        }
        return orderRepository.findByStatusOrderByIdDesc(status, pageable);
    }

    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // ==================== 私有方法 ====================
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
        if (!m.matches()) throw new BusinessException("座位格式错误：" + label);
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