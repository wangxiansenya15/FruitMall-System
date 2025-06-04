package com.wxs.controller;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.ContactMessage;
import com.wxs.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msg")
@Slf4j
public class MsgController {
    @Autowired
    private MessageService messageService;
    @PostMapping("/contact")
    public Result<?> leaveMsg(@RequestBody ContactMessage contactMsg) {
        log.info("开始处理留言请求,ContactMessage:{}", contactMsg);

        return messageService.insertContactMsg(contactMsg);
    }
}
