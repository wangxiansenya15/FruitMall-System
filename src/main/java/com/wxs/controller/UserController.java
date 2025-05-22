package com.wxs.controller;

import com.github.pagehelper.PageInfo;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserStatus;
import com.wxs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/users")
public class UserController {
    @Autowired
    private UserService userService;

    // 创建用户（POST /users）
    @PostMapping
    public Result<User> insertUser(@RequestBody User user) {
        userService.insertUser(user);
        return Result.success("用户创建成功", user);
    }

    // 删除用户（DELETE /users/{id}）
    @DeleteMapping("/{id}")
    public Result<?> deleteUserById(@PathVariable Integer id) {
        return userService.deleteUserById(id)
                ? Result.success()
                : Result.notFound("用户不存在");
    }

    // 更新用户（PUT /users/{id}）
    @PutMapping("/{id}")
    public Result<?> updateUser(@RequestBody User user, @PathVariable Integer id) {
        if (!id.equals(user.getId())) {
            return Result.requestError("ID不匹配");
        }
        if (userService.getUserById(id) == null) {
            return Result.notFound("用户不存在");
        }
        boolean updated = userService.updateUser(user);
        return updated ? Result.success("修改成功") : Result.serverError("更新失败，请重试");
    }


    // 获取所有用户（GET /users）

    public Result<List<User>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }


    @GetMapping
    public Result<PageInfo<User>> list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size) {
        PageInfo<User> pageInfo = userService.getUserList(page, size);
        return Result.success(pageInfo);
    }


    // 获取单个用户（GET /users/{id}）
    @GetMapping("/{id}")
    public Result<?> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return user != null
                ? Result.success(user)
                : Result.notFound("用户不存在");
    }

    // 更新用户状态（PATCH /users/{id}/status）
    @PatchMapping("/{id}/status")
    public Result<UserStatus> updateUserStatus(@PathVariable Integer id, @RequestBody Map<String, Object> statusData) {
        // 从请求体中获取状态信息
        log.info("接收到用户状态更新请求，用户ID: {}, 状态数据: {}", id, statusData);
        
        // 获取状态值，如果前端直接传了status枚举字符串
        UserStatus status = null;
        if (statusData.containsKey("status") && statusData.get("status") instanceof String) {
            try {
                status = UserStatus.valueOf((String) statusData.get("status"));
            } catch (IllegalArgumentException e) {
                log.warn("无效的状态值: {}", statusData.get("status"));
                return Result.fail(400,"无效的状态值");
            }
        }
        
        // 如果没有直接传status枚举，则从布尔值构建
        if (status == null) {
            boolean enabled = true;
            boolean accountNonExpired = true;
            boolean accountNonLocked = true;
            boolean credentialsNonExpired = true;
            
            if (statusData.containsKey("enabled") && statusData.get("enabled") instanceof Boolean) {
                enabled = (Boolean) statusData.get("enabled");
            }
            if (statusData.containsKey("accountNonExpired") && statusData.get("accountNonExpired") instanceof Boolean) {
                accountNonExpired = (Boolean) statusData.get("accountNonExpired");
            }
            if (statusData.containsKey("accountNonLocked") && statusData.get("accountNonLocked") instanceof Boolean) {
                accountNonLocked = (Boolean) statusData.get("accountNonLocked");
            }
            if (statusData.containsKey("credentialsNonExpired") && statusData.get("credentialsNonExpired") instanceof Boolean) {
                credentialsNonExpired = (Boolean) statusData.get("credentialsNonExpired");
            }
            
            status = UserStatus.fromBooleans(enabled, accountNonExpired, accountNonLocked, credentialsNonExpired);
        }
        
        log.info("解析后的用户状态: {}", status);
        return userService.updateStatus(id, status);
    }
}