package com.starscreen.controller;

import com.starscreen.common.Result;
import com.starscreen.dto.LoginRequest;
import com.starscreen.dto.RegisterRequest;
import com.starscreen.dto.UserVO;
import com.starscreen.entity.User;
import com.starscreen.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 注册 */
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody @Valid RegisterRequest req) {
        User user = userService.register(req);
        return Result.success(UserVO.from(user));
    }

    /** 登录 */
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody @Valid LoginRequest req, HttpSession session) {
        User user = userService.login(req);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        return Result.success(UserVO.from(user));
    }

    /** 登出 */
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.success();
    }

    /** 获取当前登录用户 */
    @GetMapping("/current")
    public Result<UserVO> current(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(UserVO.from(userService.getById((Long) userId)));
    }
}