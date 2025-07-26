package com.fantasy.rabbitpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 489450270774081641L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;
}
