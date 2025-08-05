package com.fantasy.rabbitpicturebackend.utils;

import com.fantasy.rabbitpicturebackend.exception.BusinessException;
import com.fantasy.rabbitpicturebackend.exception.ErrorCode;

import java.util.Random;

/**
 * 随机生成验证码工具类
 */
public class VerificationCodeUtil {

    /**
     * 生成指定位数的纯数字验证码
     *
     * @param length 验证码长度
     * @return 随机数字验证码
     */
    public static String generateCode(int length) {
        if (length <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码长度必须大于0");
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成0-9的随机数
        }

        return sb.toString();
    }
}