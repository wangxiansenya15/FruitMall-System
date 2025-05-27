package com.wxs.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product")
public class Product {

    @TableId(value = "product_id", type = IdType.AUTO)
    private int id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "price")
    private int price;

    @TableField(value = "category")
    private String category;

    @TableField(value = "count")
    private int count;

    @TableField(value = "remark")
    private String description;

    @TableField(value = "image_url")
    private String imageUrl;
}
