package com.wxs.service;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wxs.pojo.dto.Result;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class EmailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送验证码到指定邮箱并存储在会话中
     *
     * @param email 接收验证码的邮箱地址，不能为空
     * @param session HTTP会话对象，用于存储生成的验证码
     * @return JSON格式的响应结果，包含发送状态和消息。成功时返回验证码发送成功信息，失败时返回错误信息
     */
    public String sendVerificationCode(String email, HttpSession session) {
        if (email == null || email.isEmpty()) {
            return "邮箱不能为空";
        }
        // 生成6位随机验证码
        String code = RandomUtil.randomNumbers(6); // 生成纯数字验证码

        // 存储验证码到会话中
        session.setAttribute("verificationCode", code);

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2958367950@qq.com");
        message.setTo(email);
        message.setSubject("水果商城——果鲜多，给你发的注册验证码");
        message.setText("您的验证码是：" + code + "，5分钟内有效，为确保您的数据安全🔐请勿向他人泄漏。");

        try {
            javaMailSender.send(message);

            // 使用 Jackson 将 Map 转换为 JSON 字符串
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(Map.of("success", true, "message", "验证码已发送，请注意查收"));
        }
        //  处理发送邮件异常
        catch (Exception e) {
            log.error("邮件发送失败", e);
            return "{\"success\": false, \"message\": \"邮件发送失败，请检查日志\"}";
        }
    }

}