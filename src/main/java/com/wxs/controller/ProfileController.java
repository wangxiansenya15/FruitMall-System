package com.wxs.controller;

import com.wxs.pojo.dto.PasswordDTO;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.service.ImageStorageService;
import com.wxs.service.ProfileService;

import com.wxs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@Slf4j
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService  userService;

    @Autowired
    private ImageStorageService imageStorageService;

    /**
     * 获取当前登录用户实体对象
     * @return 用户实体
     */
    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        log.info("当前用户: {}", username);
        return userService.getUserByUsername(username);
    }

    /**
     * 用户获取个人信息
     * @return 用户信息
     */
    @GetMapping("/profile")
    public Result<?> getProfile() {
        User user = getCurrentUser();
        if (user == null) {
            return Result.forbidden("未认证");
        }
        return profileService.getProfileById(user);
    }

    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody User updateUser) {
        log.info("接收到用户修改个人信息的请求");
         // 从上下文中获取用户名
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Result.forbidden("未认证");
        }
        // 设置当前用户ID到更新对象中，防止ID被篡改
        updateUser.setId(currentUser.getId());
        log.info("更新的用户信息:{}",updateUser);
        return profileService.updateProfile(updateUser);
    }

    @PutMapping("/password")
    public Result<?> revisePassword(@RequestBody PasswordDTO passwordDto) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.forbidden("未认证");
        }
        return profileService.revisePassword(user.getId(), passwordDto.getOldPassword(),passwordDto.getNewPassword());
    }

    @PostMapping("/avatar")
    public Result<?> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        // 从上下文中获取用户名
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Result.forbidden("未认证");
        }
        log.info("接收到文件: {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            return Result.badRequest("文件为空");
        }
        return imageStorageService.storeFile(file, currentUser.getId());
    }
}
