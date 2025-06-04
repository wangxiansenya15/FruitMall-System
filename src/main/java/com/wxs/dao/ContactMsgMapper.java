package com.wxs.dao;

import com.wxs.pojo.entity.ContactMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ContactMsgMapper {
    @Insert("INSERT INTO contact_messages (username, email, problem, message, submit_time) VALUES (#{username}, #{email}, #{problem}, #{message}, #{submitTime})")
    int insertContactMsg(ContactMessage contactMsg);
}
