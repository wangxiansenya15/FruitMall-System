package com.wxs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxs.pojo.entity.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: wxs
 * @description: 订单数据访问接口
 */
@Repository
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 创建订单
     */
    @Insert("INSERT INTO orders (user_id, order_number, total_amount, status, shipping_address, consignee_name, consignee_phone, remark, create_time, update_time) " +
            "VALUES (#{userId}, #{orderNumber}, #{totalAmount}, #{status}, #{shippingAddress}, #{consigneeName}, #{consigneePhone}, #{remark}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertOrder(Order order);

    /**
     * 根据用户ID查询订单列表
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Order> selectOrdersByUserId(Integer userId);

    /**
     * 根据订单ID查询订单详情
     */
    @Select("SELECT * FROM orders WHERE id = #{orderId}")
    Order selectOrderById(Integer orderId);

    /**
     * 根据订单号查询订单
     */
    @Select("SELECT * FROM orders WHERE order_number = #{orderNumber}")
    Order selectOrderByOrderNumber(String orderNumber);

    /**
     * 更新订单状态
     */
    @Update("UPDATE orders SET status = #{status}, update_time = NOW() WHERE id = #{orderId}")
    int updateOrderStatus(@Param("orderId") Integer orderId, @Param("status") Integer status);

    /**
     * 更新订单支付时间
     */
    @Update("UPDATE orders SET pay_time = NOW(), status = 1, update_time = NOW() WHERE id = #{orderId}")
    int updateOrderPayTime(Integer orderId);

    /**
     * 删除订单
     */
    @Delete("DELETE FROM orders WHERE id = #{orderId}")
    int deleteOrder(Integer orderId);

    /**
     * 查询所有订单（管理员用）
     */
    @Select("SELECT * FROM orders ORDER BY create_time DESC")
    List<Order> selectAllOrders();

    /**
     * 根据状态查询订单
     */
    @Select("SELECT * FROM orders WHERE status = #{status} ORDER BY create_time DESC")
    List<Order> selectOrdersByStatus(Integer status);
}
