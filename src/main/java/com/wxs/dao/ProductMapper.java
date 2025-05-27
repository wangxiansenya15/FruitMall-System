package com.wxs.dao;

import  com.wxs.pojo.entity.Product;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ProductMapper {
    @Select("SELECT * FROM product WHERE product.product_id = #{id}")
    Product selectGoodById(Integer id);

    @Select("SELECT * FROM product WHERE name = #{name}")
    Product selectGoodByName(String name);

    @Select("SELECT * FROM product")
    List<Product> selectAllGoods();

    @Insert({
            "INSERT INTO product (name, price, image_url, count, remark, category, create_time)" +
            "VALUES (#{name}, #{price}, #{imageUrl}, #{count}, #{description}, #{category}, NOW())"
    })
    int insertGood(Product product);



    @Delete("DELETE FROM product WHERE product.product_id = #{id}")
    int deleteById(Integer id);

    @Update("UPDATE product SET name = #{name}, price = #{price}, image_url = #{imageUrl}, remark = #{description} WHERE product.product_id = #{id}")
    int updateGood(Product product);
}
