package com.wxs.controller;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
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
    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    // 获取单个用户（GET /users/{id}）
    @GetMapping("/{id}")
    public Result<?> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return user != null
                ? Result.success(user)
                : Result.notFound("用户不存在");
    }
}