package com.wxs.dao;

import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 *
 * @author: wxs
 * @date: 2025/05/25 14:05
 * @description: 用户信息Mapper持久化
 *
 */

@Repository
@Mapper
public interface ProfileMapper {

    /**
     * 根据用户ID查询用户信息
     * @param id 用户ID
     * @return 用户信息
     */

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectUserProfileById(Integer id);

    /**
     * 根据用户ID查询用户详细信息
     * @param id 用户ID
     * @return 用户详细信息
     */
    @Select("SELECT * FROM user_details WHERE id = #{id}")
    UserDetail selectUserDetailById(Integer id);


    @Select("SELECT password FROM user WHERE id = #{id}")
    String selectPasswordById(Integer id);

    @Select("SELECT avatar FROM user_details WHERE id = #{id}")
    String selectAvatarById(Integer id);

    @Update("UPDATE user_details SET avatar = #{avatar} WHERE id = #{id}")
    void updateAvatar(Integer id, String avatar);

    /**
     * 更新用户基本信息
     * @param user 用户对象，包含要更新的字段
     * @return 更新是否成功
     */
    @Update("UPDATE user SET nickname = #{nickname}, email = #{email}, phone = #{phone} WHERE id = #{id}")
    boolean updateUserProfile(User user);

    /**
     * 更新用户详细信息
     * @param userDetail 用户详细信息对象
     * @return 更新是否成功
     */
    @Update("UPDATE user_details SET age = #{age}, gender = #{gender}, description = #{description}, address = #{address} WHERE id = #{id}")
    boolean updateUserDetail(UserDetail userDetail);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    void updatePassword(Integer id, String password);

}
