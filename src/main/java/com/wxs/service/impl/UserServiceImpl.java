package com.wxs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxs.dao.UserDetailMapper;
import com.wxs.dao.UserMapper;
import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserDetail;
import com.wxs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional()
public class UserServiceImpl implements UserService {
    @Autowired
    UserDetailMapper userDetailsMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public int insertUser(User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        return userMapper.insertUser(user);
    }

    @Override
    public boolean deleteUserById(Integer id) {
        if (userMapper.deleteUserById(id)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        if (getUserById(user.getId()) == null) {
            return false; // 用户不存在
        }
        return userMapper.updateUser(user) > 0; // 递归更新
    }

    @Override
    public User getUserById(Integer id) {
        if (userMapper.getUserById(id) != null) {
            return userMapper.getUserById(id);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }


    @Override
    public boolean existsByUsername(String username) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("username", username)) > 0;
    }

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserByUsername(String username) {
        log.info("开始根据用户名 {} 查询用户", username);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            log.warn("未找到用户名为 {} 的用户", username);
        } else {
            // 延迟加载用户详情
            UserDetail detail = userDetailsMapper.selectById(user.getId());
            user.setDetails(detail);
            log.info("成功找到用户: {}", user.getUsername());
        }
        return user;
    }

}
