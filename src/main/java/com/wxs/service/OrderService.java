package com.wxs.service;

import com.wxs.dao.OrderMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author: wxs
 * @description: 订单业务逻辑类
 */
@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 创建订单
     * 使用事务确保数据一致性，如果操作失败会自动回滚
     * @param order 订单对象
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Order> createOrder(Order order) {
        try {
            // 生成订单号
            order.setOrderNumber(generateOrderNumber());
            
            // 设置默认状态为待支付
            if (order.getStatus() == null) {
                order.setStatus(0);
            }
            
            // 验证必填字段
            if (order.getUserId() == null || order.getTotalAmount() == null) {
                return Result.error(Result.BAD_REQUEST, "用户ID和订单金额不能为空");
            }
            
            if (order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error(Result.BAD_REQUEST, "订单金额必须大于0");
            }
            
            int result = orderMapper.insertOrder(order);
            if (result > 0) {
                log.info("订单创建成功，订单号：{}", order.getOrderNumber());
                return Result.success("订单创建成功", order);
            } else {
                return Result.ServerError("订单创建失败");
            }
        } catch (Exception e) {
            log.error("创建订单时发生异常：{}", e.getMessage(), e);
            return Result.ServerError("订单创建失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    @Transactional(readOnly = true)
    public Result<List<Order>> getOrdersByUserId(Integer userId) {
        if (userId == null) {
            return Result.error(Result.BAD_REQUEST, "用户ID不能为空");
        }
        
        try {
            List<Order> orders = orderMapper.selectOrdersByUserId(userId);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            log.error("查询用户订单时发生异常：{}", e.getMessage(), e);
            return Result.ServerError("查询订单失败");
        }
    }

    /**
     * 根据订单ID查询订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    @Transactional(readOnly = true)
    public Result<Order> getOrderById(Integer orderId) {
        if (orderId == null) {
            return Result.error(Result.BAD_REQUEST, "订单ID不能为空");
        }
        
        try {
            Order order = orderMapper.selectOrderById(orderId);
            if (order != null) {
                return Result.success("查询成功", order);
            } else {
                return Result.error(Result.NOT_FOUND, "订单不存在");
            }
        } catch (Exception e) {
            log.error("查询订单详情时发生异常：{}", e.getMessage(), e);
            return Result.ServerError("查询订单失败");
        }
    }

    /**
     * 根据订单号查询订单
     * @param orderNumber 订单号
     * @return 订单信息
     */
    @Transactional(readOnly = true)
    public Result<Order> getOrderByOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            return Result.error(Result.BAD_REQUEST, "订单号不能为空");
        }
        
        try {
            Order order = orderMapper.selectOrderByOrderNumber(orderNumber);
            if (order != null) {
                return Result.success("查询成功", order);
            } else {
                return Result.error(Result.NOT_FOUND, "订单不存在");
            }
        } catch (Exception e) {
            log.error("根据订单号查询订单时发生异常：{}", e.getMessage(), e);
            return Result.ServerError("查询订单失败");
        }
    }

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateOrderStatus(Integer orderId, Integer status) {
        if (orderId == null || status == null) {
            return Result.error(Result.BAD_REQUEST, "订单ID和状态不能为空");
        }
        
        if (status < 0 || status > 4) {
            return Result.error(Result.BAD_REQUEST, "订单状态值无效");
        }
        
        try {
            int result = orderMapper.updateOrderStatus(orderId, status);
            if (result > 0) {
                log.info("订单状态更新成功，订单ID：{}，新状态：{}", orderId, status);
                return Result.success("订单状态更新成功");
            } else {
                return Result.error(Result.NOT_FOUND, "订单不存在或状态未改变");
            }
        } catch (Exception e) {
            log.error("更新订单状态时发生异常：{}", e.getMessage(), e);
            return Result.serverError("更新订单状态失败");
        }
    }

    /**
     * 支付订单
     * @param orderId 订单ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> payOrder(Integer orderId) {
        if (orderId == null) {
            return Result.error(Result.BAD_REQUEST, "订单ID不能为空");
        }
        
        try {
            // 先检查订单是否存在且状态为待支付
            Order order = orderMapper.selectOrderById(orderId);
            if (order == null) {
                return Result.error(Result.NOT_FOUND, "订单不存在");
            }
            
            if (order.getStatus() != 0) {
                return Result.error(Result.BAD_REQUEST, "订单状态不允许支付");
            }
            
            int result = orderMapper.updateOrderPayTime(orderId);
            if (result > 0) {
                log.info("订单支付成功，订单ID：{}", orderId);
                return Result.success("订单支付成功");
            } else {
                return Result.serverError("订单支付失败");
            }
        } catch (Exception e) {
            log.error("支付订单时发生异常：{}", e.getMessage(), e);
            return Result.serverError("订单支付失败：" + e.getMessage());
        }
    }

    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> cancelOrder(Integer orderId) {
        if (orderId == null) {
            return Result.error(Result.BAD_REQUEST, "订单ID不能为空");
        }
        
        try {
            // 先检查订单是否存在且状态允许取消
            Order order = orderMapper.selectOrderById(orderId);
            if (order == null) {
                return Result.error(Result.NOT_FOUND, "订单不存在");
            }
            
            if (order.getStatus() == 4) {
                return Result.error(Result.BAD_REQUEST, "订单已取消");
            }
            
            if (order.getStatus() == 3) {
                return Result.error(Result.BAD_REQUEST, "订单已完成，无法取消");
            }
            
            int result = orderMapper.updateOrderStatus(orderId, 4);
            if (result > 0) {
                log.info("订单取消成功，订单ID：{}", orderId);
                return Result.success("订单取消成功");
            } else {
                return Result.serverError("订单取消失败");
            }
        } catch (Exception e) {
            log.error("取消订单时发生异常：{}", e.getMessage(), e);
            return Result.serverError("订单取消失败：" + e.getMessage());
        }
    }

    /**
     * 删除订单
     * @param orderId 订单ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteOrder(Integer orderId) {
        if (orderId == null) {
            return Result.error(Result.BAD_REQUEST, "订单ID不能为空");
        }
        
        try {
            int result = orderMapper.deleteOrder(orderId);
            if (result > 0) {
                log.info("订单删除成功，订单ID：{}", orderId);
                return Result.success("订单删除成功");
            } else {
                return Result.error(Result.NOT_FOUND, "订单不存在");
            }
        } catch (Exception e) {
            log.error("删除订单时发生异常：{}", e.getMessage(), e);
            return Result.serverError("删除订单失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有订单（管理员功能）
     * @return 所有订单列表
     */
    @Transactional(readOnly = true)
    public Result<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderMapper.selectAllOrders();
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            log.error("查询所有订单时发生异常：{}", e.getMessage(), e);
            return Result.ServerError("查询订单失败");
        }
    }

    /**
     * 根据状态查询订单
     * @param status 订单状态
     * @return 订单列表
     */
    @Transactional(readOnly = true)
    public Result<List<Order>> getOrdersByStatus(Integer status) {
        if (status == null) {
            return Result.error(Result.BAD_REQUEST, "订单状态不能为空");
        }
        
        if (status < 0 || status > 4) {
            return Result.error(Result.BAD_REQUEST, "订单状态值无效");
        }
        
        try {
            List<Order> orders = orderMapper.selectOrdersByStatus(status);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            log.error("根据状态查询订单时发生异常：{}", e.getMessage(), e);
            return Result.ServerError("查询订单失败");
        }
    }

    /**
     * 生成订单号
     * 格式：ORD + yyyyMMddHHmmss + 4位随机数
     * @return 订单号
     */
    private String generateOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000; // 生成1000-9999的随机数
        return "ORD" + timestamp + randomNum;
    }
}
