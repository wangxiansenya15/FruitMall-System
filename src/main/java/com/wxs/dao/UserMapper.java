package com.wxs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxs.pojo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("INSERT INTO user (username, password, email,role) VALUES (#{username}, #{password}, #{email}, 'USER')")
    int insertUser(User user);

    @Delete("DELETE FROM user WHERE id=#{id}")
    boolean deleteUserById(@Param("id") Integer id);

    @Update("UPDATE user SET username=#{username}, email=#{email},role=#{role}, updateInfo_time = NOW() where id=#{id}")
    int updateUser(User user);

    @Select("select * from user where id=#{id}")
    User selectUserById(Integer id);

    @Select("SELECT id, username, email, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, register_time, updateInfo_time as updateTime FROM user")
    List<User> selectAllUsers();

    @Update("UPDATE user SET enabled=#{enabled}, account_non_expired=#{accountNonExpired}, account_non_locked=#{accountNonLocked}, credentials_non_expired=#{credentialsNonExpired} WHERE id=#{id}")
    int updateStatus(@Param("id") Integer id,
                     @Param("enabled") boolean enabled,
                     @Param("accountNonExpired") boolean accountNonExpired,
                     @Param("accountNonLocked") boolean accountNonLocked,
                     @Param("credentialsNonExpired") boolean credentialsNonExpired);

    @Select("select id from user where username= #{username}")
    int selectIdByUsername(String username);

    @Insert("INSERT INTO user_details (id) VALUES (#{id})")
    int insertUserDetail(Integer id);
}
