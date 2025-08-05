package com.fantasy.rabbitpicturebackend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 邮件发送工具类
 */
@Component
@Slf4j
public class EmailUtils {

    @Value("${spring.mail.username}")
    private String FromUsername;

    @Resource
    private JavaMailSender javaMailSender;

    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(FromUsername);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("【兔子云图库】邮箱验证码");
        simpleMailMessage.setText("您的验证码是：" + code + "，有效期5分钟，请勿泄露！");
        log.info("邮箱：{}，验证码：{}", toEmail, code);
        javaMailSender.send(simpleMailMessage);
    }
}
