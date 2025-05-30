package com.wxs.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data

@TableName("cart_items")
public class Cart {
    @TableId(value = "cart_items_id")
    private int id;

    @TableField(value = "user_id")
    private int userId;

    @TableField(value = "product_id")
    private int productId;

    @TableField(value = "quantity")
    private int quantity;

    @TableField(value = "unit_price")
    private double price;

    @TableField(value = "added_time")
    private Date addTime;
}
