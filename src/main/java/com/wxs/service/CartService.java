package com.wxs.service;

import com.wxs.dao.CartMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CartService {
    @Autowired
    private CartMapper cartMapper;

    public Result<Void> insertCart(Cart cart) {
        return cartMapper.insertCart(cart) > 0
                ? Result.success("已成功添加到购物车")
                : Result.serverError("添加到购物车失败");
    }

    public Result<List<Cart>> selectCart(Integer userId) {
        if(userId == null) {
            return Result.error(Result.BAD_REQUEST,"您还未登录，请先登录");
        }
        List<Cart> cart = cartMapper.selectCartByUserId(userId);
        log.info("cart:{}", cart);
        return Result.success(cart);
    }

    public Result<Void> updateCart(Integer id, Integer quantity) {
        return cartMapper.updateCartItemQuantityAndPrice(id, quantity) > 0
                ? Result.success("已更新购物车")
                : Result.serverError("更新购物车失败");
    }

    public Result<Void> deleteCart(Integer id) {
        return cartMapper.deleteCartItemById(id) > 0
                ? Result.success("已从购物车删除")
                : Result.serverError("从购物车删除失败");
    }

    public Result<Void> deleteAllCart(Integer userId) {
        return cartMapper.deleteAllCartByUserId(userId) > 0
                ? Result.success("购物车已清空")
                : Result.serverError("清空购物车失败");
    }

}
