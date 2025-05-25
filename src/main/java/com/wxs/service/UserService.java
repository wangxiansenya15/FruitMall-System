package com.wxs.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.wxs.dao.UserMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserStatus;
import com.wxs.util.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * 插入用户
     *
     * @param user 用户对象
     * @return 插入的行数
     */
    public int insertUser(User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        return userMapper.insertUser(user);
    }


    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否删除成功
     */
    public boolean deleteUserById(Integer id) {
        return userMapper.deleteUserById(id);
    }


    /**
     * 更新用户
     *
     * @param user 用户对象
     * @return 是否更新成功
     */
    public boolean updateUser(User user) {
        if (getUserById(user.getId()) == null) {
            return false; // 用户不存在
        }
        return userMapper.updateUser(user) > 0; // 递归更新
    }

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    public User getUserById(Integer id) {
        if (userMapper.getUserById(id) != null) {
            return userMapper.getUserById(id);
        }
        return null;
    }


    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    public PageInfo<User> getUserList(int page, int size) {
        PageUtils.startPage(page, size);
        List<User> list = userMapper.getAllUsers();
        return PageUtils.getPageInfo(list);
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
            user = userMapper.getUserById(user.getId());
            user.setRole(user.getRole());
            log.info("成功找到用户: {}", user.getUsername());
        }
        return user;
    }

    public Result<UserStatus> updateStatus(Integer id, UserStatus status) {
        log.info("更新用户状态，用户ID: {}, 状态: {}", id, status);
        
        // 检查用户是否存在
        User user = getUserById(id);
        if (user == null) {
            log.warn("用户不存在，ID: {}", id);
            return Result.fail(404, "用户不存在");
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
                User updatedUser = getUserById(id);
                log.info("更新后的状态值 - enabled: {}, accountNonExpired: {}, accountNonLocked: {}, credentialsNonExpired: {}",
                        updatedUser.isEnabled(), updatedUser.isAccountNonExpired(), 
                        updatedUser.isAccountNonLocked(), updatedUser.isCredentialsNonExpired());
                
                log.info("用户状态更新成功，用户ID: {}", id);
                return Result.success("用户状态更新成功", status);
            } else {
                log.warn("用户状态更新失败，SQL执行未影响任何行，用户ID: {}", id);
                return Result.fail(400, "用户状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户状态时发生异常，用户ID: {}, 异常信息: {}", id, e.getMessage(), e);
            return Result.fail(500, "更新用户状态时发生异常: " + e.getMessage());
        }
    }
}
