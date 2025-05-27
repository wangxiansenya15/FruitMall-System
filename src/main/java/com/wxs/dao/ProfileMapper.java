package com.wxs.dao;

import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否更新成功
     */
    @Update("UPDATE user SET nickname = #{nikename}, email = #{email},phone_number = #{phone} WHERE id = #{id}")
    boolean updateUserProfile(User user);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    boolean updateUserPassword(Integer id, String password);

    @Insert("INSERT INTO user_details (id, description, avatar, address) VALUES (#{id}, #{description}, #{avatar}, #{address})")
    boolean insertUserDetail(UserDetail userDetail);
}
