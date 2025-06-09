package com.wxs.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 */
@Data
@TableName("orders")
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "order_number")
    private String orderNumber;

    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    @TableField(value = "status")
    private Integer status; // 0-待支付, 1-已支付, 2-已发货, 3-已完成, 4-已取消

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(value = "pay_time")
    private Date payTime;

    @TableField(value = "shipping_address")
    private String shippingAddress;

    @TableField(value = "consignee_name")
    private String consigneeName;

    @TableField(value = "consignee_phone")
    private String consigneePhone;

    @TableField(value = "remark")
    private String remark;
}