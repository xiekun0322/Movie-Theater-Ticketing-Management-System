package com.starscreen.controller;

import com.starscreen.common.Result;
import com.starscreen.dto.LoginRequest;
import com.starscreen.dto.RegisterRequest;
import com.starscreen.entity.User;
import com.starscreen.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /** 注册 */
    @PostMapping("/register")
    public Result<User> register(@RequestBody @Valid RegisterRequest req) {
        User user = userService.register(req);
        return Result.success(user);
    }

    /** 登录 */
    @PostMapping("/login")
    public Result<User> login(@RequestBody @Valid LoginRequest req, HttpSession session) {
        User user = userService.login(req);
        // 登录成功 → 把 userId 放到 session
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        return Result.success(user);
    }

    /** 登出 */
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.success();
    }

    /** 获取当前登录用户 */
    @GetMapping("/current")
    public Result<User> current(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(userService.getById((Long) userId));
    }
}
