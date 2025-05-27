package com.wxs.controller;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.service.ProfileService;

import com.wxs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService  userService;

    /**
     * 用户获取个人信息
     * @return 用户信息
     */
    @GetMapping("/profile")
    public Result<?> getProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.forbidden("未认证");
        }

        String username = authentication.getName(); // 从上下文中获取用户名
        log.info("当前用户: {}", username);

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.error(Result.BAD_REQUEST,"用户不存在");
        }


        return profileService.getProfileById(user);
    }
}
