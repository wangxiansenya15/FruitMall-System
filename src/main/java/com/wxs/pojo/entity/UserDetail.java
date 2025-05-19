package com.wxs.pojo.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_details")
public class UserDetail {

    @TableField(value = "description")
    private String description;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "role")
    private Role role;
}
