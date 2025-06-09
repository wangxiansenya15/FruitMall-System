package com.wxs.controller;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Order;
import com.wxs.pojo.entity.User;
import com.wxs.service.OrderService;
import com.wxs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: wxs
 * @description: 订单控制器，处理订单相关的HTTP请求
 */
@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户实体对象
     * 通过Spring Security上下文获取当前认证用户信息
     * @return 用户实体，如果未登录返回null
     */
    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        log.info("当前用户: {}", username);
        return userService.getUserByUsername(username);
    }

    /**
     * 创建订单
     * POST /orders
     * @param order 订单信息
     * @return 创建结果
     */
    @PostMapping
    public Result<Order> createOrder(@RequestBody Order order) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "请先登录");
        }
        
        // 设置当前用户ID到订单中
        order.setUserId(user.getId());
        
        log.info("用户 {} 创建订单，订单信息: {}", user.getUsername(), order);
        return orderService.createOrder(order);
    }

    /**
     * 获取当前用户的订单列表
     * GET /orders
     * @return 订单列表
     */
    @GetMapping
    public Result<List<Order>> getUserOrders() {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "请先登录");
        }
        
        log.info("用户 {} 查询订单列表", user.getUsername());
        return orderService.getOrdersByUserId(user.getId());
    }

    /**
     * 根据订单ID获取订单详情
     * GET /orders/{orderId}
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/{orderId}")
    public Result<Order> getOrderById(@PathVariable Integer orderId) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "请先登录");
        }
        
        // 先获取订单信息
        Result<Order> orderResult = orderService.getOrderById(orderId);
        if (orderResult.getCode() != Result.OK) {
            return orderResult;
        }
        
        Order order = orderResult.getData();
        // 验证订单是否属于当前用户（安全检查）
        if (!order.getUserId().equals(user.getId())) {
            return Result.error(Result.FORBIDDEN, "无权访问此订单");
        }
        
        log.info("用户 {} 查询订单详情，订单ID: {}", user.getUsername(), orderId);
        return orderResult;
    }

    /**
     * 根据订单号获取订单详情
     * GET /orders/number/{orderNumber}
     * @param orderNumber 订单号
     * @return 订单详情
     */
    @GetMapping("/number/{orderNumber}")
    public Result<Order> getOrderByOrderNumber(@PathVariable String orderNumber) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "请先登录");
        }
        
        // 先获取订单信息
        Result<Order> orderResult = orderService.getOrderByOrderNumber(orderNumber);
        if (orderResult.getCode() != Result.OK) {
            return orderResult;
        }
        
        Order order = orderResult.getData();
        // 验证订单是否属于当前用户（安全检查）
        if (!order.getUserId().equals(user.getId())) {
            return Result.error(Result.FORBIDDEN, "无权访问此订单");
        }
        
        log.info("用户 {} 根据订单号查询订单，订单号: {}", user.getUsername(), orderNumber);
        return orderResult;
    }

    /**
     * 支付订单
     * PUT /orders/{orderId}/pay
     * @param orderId 订单ID
     * @return 支付结果
     */
    @PutMapping("/{orderId}/pay")
    public Result<Void> payOrder(@PathVariable Integer orderId) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "请先登录");
        }
        
        // 先验证订单是否属于当前用户
        Result<Order> orderResult = orderService.getOrderById(orderId);
        if (orderResult.getCode() != Result.OK) {
            return Result.error(orderResult.getCode(), orderResult.getMessage());
        }
        
        Order order = orderResult.getData();
        if (!order.getUserId().equals(user.getId())) {
            return Result.error(Result.FORBIDDEN, "无权操作此订单");
        }
        
        log.info("用户 {} 支付订单，订单ID: {}", user.getUsername(), orderId);
        return orderService.payOrder(orderId);
    }

    /**
     * 取消订单
     * PUT /orders/{orderId}/cancel
     * @param orderId 订单ID
     * @return 取消结果
     */
    @PutMapping("/{orderId}/cancel")
    public Result<Void> cancelOrder(@PathVariable Integer orderId) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "请先登录");
        }
        
        // 先验证订单是否属于当前用户
        Result<Order> orderResult = orderService.getOrderById(orderId);
        if (orderResult.getCode() != Result.OK) {
            return Result.error(orderResult.getCode(), orderResult.getMessage());
        }
        
        Order order = orderResult.getData();
        if (!order.getUserId().equals(user.getId())) {
            return Result.error(Result.FORBIDDEN, "无权操作此订单");
        }
        
        log.info("用户 {} 取消订单，订单ID: {}", user.getUsername(), orderId);
        return orderService.cancelOrder(orderId);
    }

    /**
     * 删除订单
     * DELETE /orders/{orderId}
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/{orderId}")
    public Result<Void> deleteOrder(@PathVariable Integer orderId) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "请先登录");
        }
        
        // 先验证订单是否属于当前用户
        Result<Order> orderResult = orderService.getOrderById(orderId);
        if (orderResult.getCode() != Result.OK) {
            return Result.error(orderResult.getCode(), orderResult.getMessage());
        }
        
        Order order = orderResult.getData();
        if (!order.getUserId().equals(user.getId())) {
            return Result.error(Result.FORBIDDEN, "无权操作此订单");
        }
        
        // 只允许删除已取消的订单
        if (order.getStatus() != 4) {
            return Result.error(Result.BAD_REQUEST, "只能删除已取消的订单");
        }
        
        log.info("用户 {} 删除订单，订单ID: {}", user.getUsername(), orderId);
        return orderService.deleteOrder(orderId);
    }

    /**
     * 根据状态获取当前用户的订单列表
     * GET /orders/status/{status}
     * @param status 订单状态
     * @return 订单列表
     */
    @GetMapping("/status/{status}")
    public Result<List<Order>> getOrdersByStatus(@PathVariable Integer status) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.UNAUTHORIZED, "请先登录");
        }
        
        // 获取所有指定状态的订单，然后过滤出当前用户的订单
        Result<List<Order>> ordersResult = orderService.getOrdersByStatus(status);
        if (ordersResult.getCode() != Result.OK) {
            return ordersResult;
        }
        
        List<Order> userOrders = ordersResult.getData().stream()
                .filter(order -> order.getUserId().equals(user.getId()))
                .toList();
        
        log.info("用户 {} 查询状态为 {} 的订单列表", user.getUsername(), status);
        return Result.success("查询成功", userOrders);
    }
}