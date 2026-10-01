package com.starscreen.dto;

import com.starscreen.entity.User;
import lombok.Data;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String phone;
    private String createTime;

    /** 从 User 转成 UserVO（不暴露 password） */
    public static UserVO from(User user) {
        if (user == null) return null;
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setPhone(user.getPhone());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }
}
