package com.wxs.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// 订单核心数据载体
@Data
public class OrderDTO {
    private String orderNo;
    private UserDTO buyer;
    private List<ProductDTO> items;
    private BigDecimal totalAmount;
    private Integer orderStatus; // 1-待支付 2-已发货 3-已完成
    private LocalDateTime createTime;
}