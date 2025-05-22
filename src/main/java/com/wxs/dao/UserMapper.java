package com.wxs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserStatus;
import jdk.jshell.Snippet;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("INSERT INTO user (username, password, email) VALUES (#{username}, #{password}, #{email})")
    int insertUser(User user);

    @Delete("DELETE FROM user WHERE id=#{id}")
    boolean deleteUserById(@Param("id") Integer id);

    @Update("UPDATE user SET username=#{username},password=#{password}, email=#{email} where id=#{id}")
    int updateUser(User user);

    @Select("select * from user where id=#{id}")
    User getUserById(Integer id);

    @Select("select * from user")
    List<User> getAllUsers();

    @Update("UPDATE user SET enabled=#{enabled}, account_non_expired=#{accountNonExpired}, " +
            "account_non_locked=#{accountNonLocked}, credentials_non_expired=#{credentialsNonExpired} WHERE id=#{id}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int updateStatus(Integer id,
                     @Param("enabled") boolean enabled,
                     @Param("accountNonExpired") boolean accountNonExpired,
                     @Param("accountNonLocked") boolean accountNonLocked,
                     @Param("credentialsNonExpired") boolean credentialsNonExpired);
}
