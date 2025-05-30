package com.wxs.controller;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Cart;
import com.wxs.pojo.entity.User;
import com.wxs.service.CartService;
import com.wxs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户实体对象
     * @return 用户实体
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

    @PostMapping("/items")
    public Result<?> addCartItem(@RequestBody Cart cart) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.BAD_REQUEST,"您还未登录，请先登录");
        }

        cart.setUserId(user.getId());
        cart.setProductId(cart.getProductId());
        cart.setQuantity(cart.getQuantity());
        cart.setPrice(cart.getPrice());
        cart.setAddTime(cart.getAddTime());
        log.info("接收到用户添加购物车的请求,cart对象为：{}",  cart);
        return cartService.insertCart(cart);
    }

    @PutMapping("/items/{itemId}")
    public Result<?> updateCartItem(@PathVariable Integer itemId,@RequestBody Cart cart) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.BAD_REQUEST,"您还未登录，请先登录");
        }
        log.info("用户更新购物车请求,itemId:{}, quantity:{}", itemId, cart.getQuantity());
        return cartService.updateCart(itemId, cart.getQuantity());
    }

    @GetMapping("/items")
    public Result<?> getCartItems() {
        log.info("接收到用户查询购物车请求");
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.BAD_REQUEST,"您还未登录，请先登录");
        }
        log.info("用户查询购物车请求，用户id:{}", user.getId());
        return cartService.selectCart(user.getId());
    }

    @DeleteMapping("/items/{itemId}")
    public Result<?> deleteCartItem(@PathVariable Integer itemId) {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.BAD_REQUEST,"您还未登录，请先登录");
        }
        log.info("用户删除购物车请求,itemId:{}", itemId);
        return cartService.deleteCart(itemId);
    }

    @DeleteMapping("/items")
    public Result<?> deleteAllCartItems() {
        User user = getCurrentUser();
        if (user == null) {
            return Result.error(Result.BAD_REQUEST,"您还未登录，请先登录");
        }
        return cartService.deleteAllCart(user.getId());
    }
}
