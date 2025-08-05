package com.fantasy.rabbitpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户忘记密码请求
 */
@Data
public class ResetPasswordRequest implements Serializable {

    private static final long serialVersionUID = 357366226076565997L;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkPassword;
}
