package com.wxs.service;

import com.wxs.dao.ContactMsgMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.ContactMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author wxs
 * @date 2025/06/04 10:47
 */
@Service
@Slf4j
public class MessageService {
    @Autowired
    private ContactMsgMapper contactMsgMapper;

    public Result<ContactMessage> insertContactMsg(ContactMessage contactMsg) {
        if(contactMsg == null){
            return Result.error(Result.BAD_REQUEST,"参数错误");
        }
        return contactMsgMapper.insertContactMsg(contactMsg) > 0
                ? Result.success("提交成功")
                : Result.error(Result.INTERNAL_ERROR,"提交失败");
    }
}
