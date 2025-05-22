package com.wxs.dao;

import com.wxs.pojo.entity.User;
import com.wxs.pojo.entity.UserDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserDetailMapper {

    @Select("select * from user_details where id = #{id}")
    UserDetail selectById(Integer id);


    @Insert("insert into user_details (description, avatar, role) values (#{description}, #{avatar}, #{role})")
    int insertUserDetail(User userDetail);
}
