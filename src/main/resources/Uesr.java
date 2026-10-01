package com.starscreen.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户实体
 */
@Data
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户名，唯一 */
    @Column(unique = true, length = 50, nullable = false)
    private String username;

    /** 密码（演示环境用明文，生产环境要加密） */
    @Column(length = 100, nullable = false)
    private String password;

    /** 手机号 */
    @Column(length = 20)
    private String phone;

    /** 注册时间 */
    @Column(length = 30)
    private String createTime;
}
