package com.wxs.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("shop")
public class StoreInfo {
    @TableId(value = "id")
    private Integer id;

    @TableField(value = "store_name")
    private String name;

    @TableField(value = "address")
    private String address;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "status")
    private String status;

    @TableField(value = "description")
    private String description;

    @TableField(value = "open_time")
    private String open_time;

    @TableField(value = "close_time")
    private String close_time;
}
