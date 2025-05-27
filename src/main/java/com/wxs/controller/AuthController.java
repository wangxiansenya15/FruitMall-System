package com.wxs.controller;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.dto.UserDTO;
import com.wxs.pojo.entity.User;
import com.wxs.service.AuthenticationService;
import com.wxs.service.EmailService;
import com.wxs.service.UserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authService;
    private final EmailService emailService;

    // 推荐使用构造函数注入
    public AuthController(UserService userService, AuthenticationService authService, EmailService emailService) {
        this.userService = userService;
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/code")
    public String getVerificationCode(@RequestBody Map<String, String> payload, HttpSession session) {
        String email = payload.get("email");
        return emailService.sendVerificationCode(email, session);
    }

    @PostMapping("/register")
    public Result<?> registerUser(@RequestBody User user) {
        return userService.insertUser(user);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody UserDTO userDTO) {
        log.info("开始处理登录请求，用户名: {}", userDTO.getUsername());
        return authService.authenticate(userDTO);
    }

    @PostMapping("/msg")
    public String sendNotification(@RequestBody Map<String, String> payload, HttpSession session) {
        return emailService.sendDisciplinaryNotice(payload, session);
    }
}