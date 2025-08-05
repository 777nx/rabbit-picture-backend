package com.fantasy.rabbitpicturebackend.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    /**
     * 邮箱验证码 key 前缀
     */
    String EMAIL_KEY_PREFIX = "rabbitPicture:sendEmail:code:";

    /**
     * 邮箱验证码冷却期 key 前缀
     */
    String EMAIL_TIMER_KEY_PREFIX = "rabbitPicture:sendEmail:timer:";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    // endregion
}