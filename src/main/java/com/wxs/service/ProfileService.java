package com.wxs.service;

import com.wxs.dao.ProfileMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfileService {
    @Autowired
    ProfileMapper profileMapper;

    /**
     * 获取用户个人信息
     * @return 用户信息
     */

    public Result<User> getProfileById(User user) {
        if(user.getId() == null){
            log.error("用户ID为空");
            return Result.error(Result.FORBIDDEN,"用户登录信息过期，请重新登录");
        }
        user=profileMapper.selectUserProfileById(user.getId());
        UserDetail detail= profileMapper.selectUserDetailById(user.getId());
        user.setDetails(detail);
        log.info("用户信息已成功加载,用户:{}",user);
        return Result.success("用户信息已成功加载",user);
    }
}
