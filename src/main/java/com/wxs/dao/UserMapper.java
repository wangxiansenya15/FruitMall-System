package com.wxs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxs.pojo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("INSERT INTO user (username, password, gender, email) VALUES (#{username}, #{password}, #{gender}, #{email})")
    int insertUser(User user);

    @Delete("delete from user where id=#{id}")
    boolean deleteUserById(@Param("id") Integer id);

    @Update("update user set username=#{username},password=#{password},gender=#{gender},email=#{email} where id=#{id}")
    int updateUser(User user);

    @Select("select * from user where id=#{id}")
    User getUserById(Integer id);

    @Select("select * from user")
    List<User> getAllUsers();
}
