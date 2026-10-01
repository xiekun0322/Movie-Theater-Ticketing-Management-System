package com.starscreen.service;

import com.starscreen.common.BusinessException;
import com.starscreen.dto.LoginRequest;
import com.starscreen.dto.RegisterRequest;
import com.starscreen.entity.User;
import com.starscreen.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserRepository userRepository;

    /**
     * 注册
     */
    @Transactional
    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());  // 演示用明文；生产要 BCrypt
        user.setPhone(req.getPhone());
        user.setCreateTime(LocalDateTime.now().format(DTF));
        user = userRepository.save(user);

        log.info("用户注册成功：id={}, username={}", user.getId(), user.getUsername());
        return user;
    }

    /**
     * 登录
     */
    public User login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        log.info("用户登录成功：id={}, username={}", user.getId(), user.getUsername());
        return user;
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
