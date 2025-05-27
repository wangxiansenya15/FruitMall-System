package com.wxs.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_details")
public class UserDetail {
    @TableField(value = "id")
    private Integer id;

    @TableField(value = "description")
    private String description;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "gender")
    private String gender;

    @TableField(value = "address")
    private String address;
}
