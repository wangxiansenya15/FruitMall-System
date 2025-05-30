package com.wxs.dao;

import com.wxs.pojo.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("SELECT product_id as id, name, price, remark as description, category, count as stock, image_url as imageUrl, rating FROM product WHERE product_id = #{id}")
    Product selectGoodById(Integer id);

    @Select("SELECT * FROM product WHERE name = #{name}")
    Product selectGoodByName(String name);

    /**
     * 查询所有商品信息
     * 返回包含商品完整信息的列表，用于前端商品展示
     */
    @Select("SELECT product_id as id, name, price, remark as description, category, count as stock, image_url as imageUrl, rating FROM product")
    List<Product> selectAllGoods();

    /**
     * 获取所有商品分类
     * 从商品表中聚合获取不重复的分类列表
     */
    @Select("SELECT DISTINCT category FROM product WHERE category IS NOT NULL AND category != '' ORDER BY category")
    List<String> selectAllCategories();

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
