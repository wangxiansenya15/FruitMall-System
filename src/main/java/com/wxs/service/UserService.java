package com.wxs.service;

import com.wxs.dao.UserMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.pojo.dto.UserStatus;
import com.wxs.util.PageUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 使用事务确保数据一致性，如果操作失败会自动回滚
     * @param user 用户对象
     *             必填：用户名、密码
     *             可选：其他字段（如角色、状态等）
     * @return Result<User> 表示插入结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<User> insertUser(User user) {
        log.info("开始插入用户，用户名: {}, 邮箱: {}", user.getUsername(), user.getEmail());

        // 参数验证
        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.error(Result.BAD_REQUEST, "用户名或密码不能为空");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 验证用户名是否已存在
        if (existsByUsername(user.getUsername())) {
            log.warn("用户名已存在: {}", user.getUsername());
            return Result.error(Result.BAD_REQUEST, "用户名已存在");
        }
        
        // 验证邮箱是否已被注册
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            User existingUser = getUserByEmail(user.getEmail());
            if (existingUser != null) {
                log.warn("邮箱已被注册: {}", user.getEmail());
                return Result.error(Result.BAD_REQUEST, "邮箱已注册");
            }
        }
        
        try {
            // 插入用户记录
            int insertedUser = userMapper.insertUser(user);
            user.setId(userMapper.selectIdByUsername(user.getUsername()));
            int insertedDetail = userMapper.insertUserDetail(user.getId());
            
            if ((insertedUser & insertedDetail) > 0) {
                log.info("用户创建成功，用户名: {}", user.getUsername());
                return Result.success("用户创建成功", user);
            }
            
            log.error("用户创建失败，用户名: {}", user.getUsername());
            return Result.ServerError("用户创建失败,请稍后重试");
        } catch (Exception e) {
            log.error("用户创建过程中发生异常，用户名: {}", user.getUsername(), e);
            throw e; // 让事务管理器处理回滚
        }
    }


    /**
     * 根据ID删除用户
     * 使用事务确保数据一致性，如果操作失败会自动回滚
     * @param id 用户ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteUserById(Integer id) {
        if (id == null) {
            return Result.badRequest( "ID不能为空");
        }
        return userMapper.deleteUserById(id)
                ? Result.success("删除成功" )
                : Result.serverError("删除失败");
    }

    /**
     * 更新用户信息
     * 使用事务确保数据一致性，如果操作失败会自动回滚
     * @param user 用户信息
     * @return Result<Void>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateUser(User user) {
        if (user.getId() == null) {
            return Result.badRequest("用户ID不能为空");
        }

        if (getUserById(user.getId()) == null) {
            return Result.notFound("用户不存在");
        }
        return userMapper.updateUser(user) > 0 ?
                Result.success("更新成功") :
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
            return Result.ServerError("获取用户失败");
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
            log.info("获取用户列表成功，用户list: {}", list);
            return Result.success(PageUtils.getPageInfo(list));
        } catch (RuntimeException e) {
            log.error("获取用户列表失败", e);
            return Result.ServerError("获取用户列表失败");
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

    /**
     * 根据邮箱获取用户
     * 用于密码重置功能中根据邮箱查找用户
     * @param email 用户邮箱
     * @return 用户对象，如果不存在则返回null
     */
    public User getUserByEmail(String email) {
        log.info("开始根据邮箱 {} 查询用户", email);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            log.warn("未找到邮箱为 {} 的用户", email);
        } else {
            // 延迟加载用户详情
            user = userMapper.selectUserById(user.getId());
            log.info("成功找到用户: {}, 邮箱: {}", user.getUsername(), email);
        }
        return user;
    }

    /**
     * 更新用户密码
     * 用于密码重置功能，直接更新用户的加密密码
     * @param userId 用户ID
     * @param encodedPassword 已加密的新密码
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPassword(Integer userId, String encodedPassword) {
        log.info("开始更新用户密码，用户ID: {}", userId);
        try {
            int updated = userMapper.updateUserPassword(userId, encodedPassword);
            if (updated > 0) {
                log.info("用户密码更新成功，用户ID: {}", userId);
                return true;
            } else {
                log.warn("用户密码更新失败，未影响任何行，用户ID: {}", userId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户密码时发生异常，用户ID: {}, 异常信息: {}", userId, e.getMessage(), e);
            return false;
        }
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
            return Result.ServerError("更新用户状态时发生异常: " + e.getMessage());
        }
    }
    
    /**
     * 处理登录失败，当失败次数达到阈值时锁定账户
     * @param username 用户名
     * @param attemptCount 当前失败次数
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> handleLoginFailure(String username, Integer attemptCount) {
        log.info("处理登录失败，用户名: {}, 失败次数: {}", username, attemptCount);
        
        // 检查失败次数是否达到锁定阈值
        if (attemptCount >= 5) {
            // 获取用户信息
            User user = getUserByUsername(username);
            if (user == null) {
                log.warn("用户不存在，无法锁定账户，用户名: {}", username);
                return Result.error(Result.NOT_FOUND, "用户不存在");
            }
            
            try {
                // 锁定账户：将 account_non_locked 设置为 false
                int updated = userMapper.updateStatus(
                        user.getId(),
                        user.isEnabled(),
                        user.isAccountNonExpired(),
                        false, // 锁定账户
                        user.isCredentialsNonExpired()
                );
                
                if (updated > 0) {
                    log.warn("账户已被锁定，用户名: {}, 失败次数: {}", username, attemptCount);
                    return Result.success("账户因多次登录失败已被锁定");
                } else {
                    log.error("锁定账户失败，用户名: {}", username);
                    return Result.error(Result.SERVER_ERROR, "锁定账户失败");
                }
            } catch (Exception e) {
                log.error("锁定账户时发生异常，用户名: {}, 异常信息: {}", username, e.getMessage(), e);
                return Result.ServerError("锁定账户时发生异常: " + e.getMessage());
            }
        } else {
            log.info("登录失败次数未达到锁定阈值，用户名: {}, 当前失败次数: {}", username, attemptCount);
            return Result.success("登录失败已记录，失败次数: " + attemptCount);
        }
    }
}
