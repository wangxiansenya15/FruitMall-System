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
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String sendVerificationCode(String email, HttpSession session) {
        if (email == null || email.isEmpty()) {
            return "邮箱不能为空";
        }
        // 生成6位随机验证码
        String code = RandomUtil.randomNumbers(6); // 生成纯数字验证码
        
        session.setAttribute("verificationCode", code);
        
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
            
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            return "{\"success\": false, \"message\": \"邮件发送失败，请检查日志\"}";
        }
    }
    
    public String sendDisciplinaryNotice(Map<String, String> payload, HttpSession session) {
        // 获取必要参数
        String email = payload.get("email");
        String studentName = payload.get("studentName");
        String violationType = payload.get("violationType");
        String punishmentDetails = payload.get("punishmentDetails");
        
        // 参数校验
        if (email == null || email.isEmpty()) {
            return "{\"success\": false, \"message\": \"邮箱不能为空\"}";
        }
        if (studentName == null || studentName.isEmpty()) {
            return "{\"success\": false, \"message\": \"学生姓名不能为空\"}";
        }
        if (violationType == null || violationType.isEmpty()) {
            return "{\"success\": false, \"message\": \"违纪类型不能为空\"}";
        }
        
        // 构建邮件内容
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2958367950@qq.com");
        message.setTo(email);
        message.setSubject("违纪处分通知 - " + studentName);
        
        String emailContent = String.format(
                "学生姓名：%s\n" +
                        "违纪类型：%s\n" +
                        "处分详情：%s\n\n" +
                        "请及时处理并回复本邮件确认接收。\n" +
                        "如有疑问，请联系学校相关部门。",
                studentName,
                violationType,
                punishmentDetails != null ? punishmentDetails : "无详细说明"
        );
        message.setText(emailContent);
        
        try {
            // 发送邮件
            javaMailSender.send(message);
            
            // 记录发送日志（可选）
            session.setAttribute("lastNotification", new Date());
            
            // 返回成功响应
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(Map.of(
                    "success", true,
                    "message", "处分通知已发送至" + email
            ));
        } catch (Exception e) {
            log.error("邮件发送失败: {}", e.getMessage(), e);
            return "{\"success\": false, \"message\": \"处分通知发送失败，请检查邮箱地址是否正确或联系管理员\"}";
        }
    }
}