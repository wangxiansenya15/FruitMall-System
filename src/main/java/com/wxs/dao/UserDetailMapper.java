package com.wxs.dao;

import com.wxs.pojo.entity.UserDetail;
import org.apache.ibatis.annotations.Select;

public interface UserDetailMapper {

    @Select("select * from user_details where id = #{id}")
    UserDetail selectById(Integer id);
}
