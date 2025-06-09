package com.wxs.service;

import com.wxs.dao.ProfileMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserDetail;

import com.wxs.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ProfileService {
    @Autowired
    ProfileMapper profileMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 获取用户个人信息
     * @return 用户信息
     */

    public Result<User> getProfileById(User user) {
        if (user == null) {
            return Result.error(Result.BAD_REQUEST,"用户不存在");
        }
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

    /**
     * 修改密码
     * 使用事务确保数据一致性，如果操作失败会自动回滚
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> revisePassword(Integer id, String oldPassword, String newPassword) {
        // 1. 参数非空校验
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.badRequest("新密码不能为空");
        }

        // 2. 获取当前用户id,密码
        if (id == null) {
            return Result.forbidden("登录信息过期，请重新登录");
        }
        String currentEncodedPassword = profileMapper.selectPasswordById(id);
        log.info("用户id:{},当前密码:{}", id, currentEncodedPassword);
        //判断用户是否存在，防止恶意请求
        if (currentEncodedPassword == null) {
            return Result.notFound("用户不存在");
        }

        // ✅ 使用 matches 进行密码校验
        boolean isMatch = passwordEncoder.matches(oldPassword, currentEncodedPassword);
        log.warn("密码比对结果：{}", isMatch);
        if (!isMatch) {
            return Result.unauthorized("当前密码不正确,请重新输入");
        }

        // 4. 加密并更新新密码
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        profileMapper.updatePassword(id, encodedNewPassword);

        return Result.success("密码修改成功");
    }


    /**
     * 更新用户资料
     * 使用事务确保数据一致性，如果操作失败会自动回滚
     * @param user 用户信息
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<User> updateProfile(User user) {
        if (user == null || user.getId() == null) {
            return Result.error(Result.FORBIDDEN,"登录信息过期，请重新登录");
        }

        try {
            // 更新用户基本信息
            boolean userUpdated = profileMapper.updateUserProfile(user);
            
            // 更新用户详细信息（如果存在）
            boolean detailUpdated = true;
            if (user.getDetails() != null) {
                user.getDetails().setId(user.getId()); // 确保详细信息的ID与用户ID一致
                detailUpdated = profileMapper.updateUserDetail(user.getDetails());
            }
            
            return (userUpdated && detailUpdated)
                    ? Result.success("个人信息更新成功")
                    : Result.ServerError("用户信息修改失败");
        } catch (Exception e) {
            log.error("更新用户信息时发生异常，用户ID: {}, 错误: {}", user.getId(), e.getMessage(), e);
            return Result.ServerError("用户信息修改失败: " + e.getMessage());
        }
    }

}
