package com.wxs.service;

import com.wxs.dao.UserMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserStatus;
import com.wxs.util.PageUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * 插入用户
     *
     * @param user 用户对象
     *             必填：用户名、密码
     *             可选：其他字段（如角色、状态等）
     * @return Result<User> 表示插入结果
     */
    public Result<User> insertUser(User user) {
        log.info("开始插入用户，用户名: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.error(Result.BAD_REQUEST, "用户名或密码不能为空");
        }
        if (existsByUsername(user.getUsername())) {
            return Result.error(Result.BAD_REQUEST, "用户名已存在");
        }
        int inserted = userMapper.insertUser(user);
        if (inserted > 0) {
            log.info("用户创建成功，用户名: {}", user.getUsername());
            return Result.success("用户创建成功",user);
        }
        log.error("用户创建失败，用户名: {}", user.getUsername());
        return Result.error(Result.INTERNAL_ERROR, "用户创建失败");
    }


    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 是否删除成功
     */
    public Result<Void> deleteUserById(Integer id) {
        if (id == null) {
            return Result.badRequest( "ID不能为空");
        }
        return userMapper.deleteUserById(id)
                ? Result.success(Result.OK,"删除成功" )
                : Result.serverError("删除失败");
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return Result<Void>
     */
    public Result<Void> updateUser(User user) {
        if (user.getId() == null) {
            return Result.badRequest("用户ID不能为空");
        }

        if (getUserById(user.getId()) == null) {
            return Result.notFound("用户不存在");
        }
        return userMapper.updateUser(user) > 0 ?
                Result.success(Result.OK,"更新成功") :
                Result.serverError("更新失败");
    }

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    public Result<User> getUserById(Integer id) {
        if (id == null) {
            return Result.error(Result.BAD_REQUEST,"ID不能为空");
        }

        try {
            User user = userMapper.selectUserById(id);
            return user != null
                    ? Result.success(user)
                    : Result.error(Result.NOT_FOUND,"用户不存在");
        } catch (Exception e) {
            log.error("获取用户失败", e);
            return Result.error(Result.INTERNAL_ERROR,"获取用户失败");
        }
    }


    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    public Result<PageInfo<User>> getUserList(int page, int size) {
        try {
            PageUtils.startPage(page, size);
            List<User> list = userMapper.selectAllUsers();
            return Result.success(PageUtils.getPageInfo(list));
        } catch (RuntimeException e) {
            log.error("获取用户列表失败", e);
            return Result.error(Result.INTERNAL_ERROR,"获取用户列表失败");
        }
    }

    public boolean existsByUsername(String username) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("username", username)) > 0;
    }


    public User getUserByUsername(String username) {
        log.info("开始根据用户名 {} 查询用户", username);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            log.warn("未找到用户名为 {} 的用户", username);
        } else {
            // 延迟加载用户详情
            user = userMapper.selectUserById(user.getId());
            user.setRole(user.getRole());
            log.info("成功找到用户: {}", user.getUsername());
        }
        return user;
    }

    public Result<UserStatus> updateStatus(Integer id, UserStatus status) {
        log.info("更新用户状态，用户ID: {}, 状态: {}", id, status);

        // 检查用户是否存在
        User user = userMapper.selectUserById(id);
        if (user == null) {
            log.warn("用户不存在，ID: {}", id);
            return Result.error(Result.NOT_FOUND, "用户不存在");
        }

        // 记录更新前的状态值，用于调试
        log.info("更新前的状态值 - enabled: {}, accountNonExpired: {}, accountNonLocked: {}, credentialsNonExpired: {}",
                user.isEnabled(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired());

        // 记录要更新的状态值
        log.info("要更新的状态值 - enabled: {}, accountNonExpired: {}, accountNonLocked: {}, credentialsNonExpired: {}",
                status.isEnabled(), status.isAccountNonExpired(), status.isAccountNonLocked(), status.isCredentialsNonExpired());

        try {
            // 执行更新操作
            log.info("执行SQL更新操作，用户ID: {}", id);
            int updated = userMapper.updateStatus(
                    id,
                    status.isEnabled(),
                    status.isAccountNonExpired(),
                    status.isAccountNonLocked(),
                    status.isCredentialsNonExpired()
            );

            log.info("SQL执行结果: updated={}", updated);

            if (updated > 0) {
                // 再次获取用户，验证更新是否生效
                User updatedUser = userMapper.selectUserById(id);
                log.info("更新后的状态值 - enabled: {}, accountNonExpired: {}, accountNonLocked: {}, credentialsNonExpired: {}",
                        updatedUser.isEnabled(), updatedUser.isAccountNonExpired(),
                        updatedUser.isAccountNonLocked(), updatedUser.isCredentialsNonExpired());

                log.info("用户状态更新成功，用户ID: {}", id);
                return Result.success("用户状态更新成功", status);
            } else {
                log.warn("用户状态更新失败，SQL执行未影响任何行，用户ID: {}", id);
                return Result.error(Result.BAD_REQUEST, "用户状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户状态时发生异常，用户ID: {}, 异常信息: {}", id, e.getMessage(), e);
            return Result.error(Result.INTERNAL_ERROR, "更新用户状态时发生异常: " + e.getMessage());
        }
    }
}
