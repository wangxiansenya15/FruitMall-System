package com.wxs.controller;

import com.wxs.pojo.dto.LoginFailureDTO;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.dto.UserDTO;
import com.wxs.pojo.entity.User;
import com.wxs.service.AuthenticationService;
import com.wxs.service.EmailService;
import com.wxs.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    
    /**
     * 处理登录失败
     * 当登录失败次数达到5次时，自动锁定用户账户
     * @param loginFailureDTO 登录失败信息
     * @return 处理结果
     */
    @PostMapping("/login-failure")
    public Result<?> handleLoginFailure(@RequestBody LoginFailureDTO loginFailureDTO) {
        log.info("处理登录失败请求，用户名: {}, 失败次数: {}, 错误类型: {}", 
                loginFailureDTO.getUsername(), loginFailureDTO.getAttemptCount(), loginFailureDTO.getErrorType());
        
        // 记录详细的失败信息用于安全审计
        log.info("登录失败详情 - IP: {}, UserAgent: {}, 时间: {}", 
                loginFailureDTO.getIpAddress(), loginFailureDTO.getUserAgent(), loginFailureDTO.getTimestamp());
        
        return userService.handleLoginFailure(loginFailureDTO.getUsername(), loginFailureDTO.getAttemptCount());
    }

    /**
     * 验证验证码
     * 用于注册时验证邮箱验证码是否正确
     * @param payload 包含邮箱和验证码的请求体
     * @param session HTTP会话对象，用于获取存储的验证码
     * @return 验证结果
     */
    @PostMapping("/verify")
    public Result<?> verifyCode(@RequestBody Map<String, String> payload, HttpSession session) {
        String email = payload.get("email");
        String verificationCode = payload.get("verificationCode");
        
        log.info("开始验证验证码，邮箱: {}", email);
        
        return authService.verifyCode(email, verificationCode, session);
    }

    /**
     * 重置密码
     * 通过验证码验证后重置用户密码
     * @param payload 包含邮箱、验证码和新密码的请求体
     * @param session HTTP会话对象，用于验证验证码
     * @return 重置结果
     */
    @PostMapping("/reset")
    public Result<?> resetPassword(@RequestBody Map<String, String> payload, HttpSession session) {
        String email = payload.get("email");
        String verificationCode = payload.get("verificationCode");
        String newPassword = payload.get("newPassword");
        
        log.info("开始重置密码，邮箱: {}", email);
        
        return authService.resetPassword(email, verificationCode, newPassword, session);
    }

}