package com.wxs.dao;

import com.wxs.pojo.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    /**
     * 添加商品到购物车
     */
    @Insert("INSERT INTO cart_items (user_id, product_id, quantity, unit_price, added_time) VALUES (#{userId}, #{productId}, #{quantity}, #{price}, NOW())")
    int insertCart(Cart cart);

    /**
     * 根据用户ID查询购物车中的所有商品
     * 使用字段别名确保正确映射到Cart实体类
     */
    @Select("SELECT cart_item_id as id, user_id as userId, product_id as productId, quantity, unit_price as price, added_time as addTime FROM cart_items WHERE user_id = #{userId}")
    List<Cart> selectCartByUserId(Integer userId);

    /**
     * 更新购物车中某个商品的数量和价格
     */
    @Update("UPDATE cart_items SET quantity = #{quantity} WHERE cart_item_id = #{id}")
    int updateCartItemQuantityAndPrice(Integer id,  Integer quantity);

    /**
     * 删除购物车中的某个商品
     */
    @Delete("DELETE FROM cart_items WHERE cart_item_id = #{id}")
    int deleteCartItemById(Integer id);


    /**
     * 删除购物车中的所有商品
     */
    @Delete("DELETE FROM cart_items WHERE user_id = #{userId}")
    int deleteAllCartByUserId(Integer id);
}
