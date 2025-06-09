package com.wxs.dao;

import com.wxs.pojo.entity.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 评论数据访问层接口
 * 提供评论相关的数据库操作方法
 */
@Mapper
public interface ReviewMapper {
    
    /**
     * 插入新评论
     * 用户发表评论时调用，自动生成ID和时间戳
     * @param review 评论对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO reviews (user_id, product_id, content, rating, create_time, update_time) " +
            "VALUES (#{userId}, #{productId}, #{content}, #{rating}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertReview(Review review);
    
    /**
     * 根据商品ID查询所有评论
     * 用于商品详情页显示所有用户评论
     * @param productId 商品ID
     * @return 评论列表
     */
    @Select("SELECT * FROM reviews WHERE product_id = #{productId} ORDER BY create_time DESC")
    List<Review> selectByProductId(Long productId);
    
    /**
     * 根据用户ID查询所有评论
     * 用于用户个人中心查看自己的评论历史
     * @param userId 用户ID
     * @return 评论列表
     */
    @Select("SELECT * FROM reviews WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Review> selectByUserId(Long userId);
    
    /**
     * 根据评论ID查询单条评论
     * 用于评论详情查看或编辑前的数据获取
     * @param id 评论ID
     * @return 评论对象
     */
    @Select("SELECT * FROM reviews WHERE id = #{id}")
    Review selectById(Long id);
    
    /**
     * 更新评论内容和评分
     * 允许用户修改自己的评论
     * @param review 包含更新信息的评论对象
     * @return 影响的行数
     */
    @Update("UPDATE reviews SET content = #{content}, rating = #{rating}, update_time = NOW() " +
            "WHERE id = #{id}")
    int updateReview(Review review);
    
    /**
     * 删除评论
     * 用户或管理员删除评论时调用
     * @param id 评论ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM reviews WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 计算商品的平均评分
     * 用于商品列表和详情页显示平均评分
     * @param productId 商品ID
     * @return 平均评分
     */
    @Select("SELECT AVG(rating) FROM reviews WHERE product_id = #{productId}")
    Double getAverageRating(Long productId);
    
    /**
     * 统计商品的评论总数
     * 用于显示商品有多少条评论
     * @param productId 商品ID
     * @return 评论总数
     */
    @Select("SELECT COUNT(*) FROM reviews WHERE product_id = #{productId}")
    int countByProductId(Long productId);
}