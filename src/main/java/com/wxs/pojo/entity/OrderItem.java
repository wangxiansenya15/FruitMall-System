package com.wxs.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单详情实体类
 */
@Data
@TableName("order_items")
public class OrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "order_id")
    private Integer orderId;

    @TableField(value = "product_id")
    private Integer productId;

    @TableField(value = "product_name")
    private String productName;

    @TableField(value = "product_image")
    private String productImage;

    @TableField(value = "price")
    private BigDecimal price;

    @TableField(value = "quantity")
    private Integer quantity;

    @TableField(value = "subtotal")
    private BigDecimal subtotal;
}