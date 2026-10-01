package com.starscreen.service;

import com.starscreen.common.BusinessException;
import com.starscreen.dto.RegisterRequest;
import com.starscreen.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("注册成功：返回用户对象，密码正确存储")
    void testRegister_Success() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("test_user_" + System.currentTimeMillis());
        req.setPassword("123456");
        req.setPhone("13800138000");

        User user = userService.register(req);

        assertNotNull(user.getId());
        assertEquals(req.getUsername(), user.getUsername());
        assertEquals("123456", user.getPassword());
        assertNotNull(user.getCreateTime());
    }

    @Test
    @DisplayName("用户名重复：抛 BusinessException")
    void testRegister_DuplicateUsername() {
        String username = "dup_user_" + System.currentTimeMillis();

        RegisterRequest req1 = new RegisterRequest();
        req1.setUsername(username);
        req1.setPassword("123456");
        userService.register(req1);

        RegisterRequest req2 = new RegisterRequest();
        req2.setUsername(username);
        req2.setPassword("654321");

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            userService.register(req2);
        });
        assertTrue(ex.getMessage().contains("已存在"));
    }

    @Test
    @DisplayName("登录成功：密码正确")
    void testLogin_Success() {
        String username = "login_user_" + System.currentTimeMillis();
        String password = "abcdef";

        RegisterRequest req = new RegisterRequest();
        req.setUsername(username);
        req.setPassword(password);
        userService.register(req);

        com.starscreen.dto.LoginRequest loginReq = new com.starscreen.dto.LoginRequest();
        loginReq.setUsername(username);
        loginReq.setPassword(password);

        User loggedIn = userService.login(loginReq);
        assertEquals(username, loggedIn.getUsername());
    }

    @Test
    @DisplayName("登录失败：密码错误")
    void testLogin_WrongPassword() {
        String username = "wrong_pwd_" + System.currentTimeMillis();

        RegisterRequest req = new RegisterRequest();
        req.setUsername(username);
        req.setPassword("correct");
        userService.register(req);

        com.starscreen.dto.LoginRequest loginReq = new com.starscreen.dto.LoginRequest();
        loginReq.setUsername(username);
        loginReq.setPassword("wrong");

        assertThrows(BusinessException.class, () -> userService.login(loginReq));
    }
}
