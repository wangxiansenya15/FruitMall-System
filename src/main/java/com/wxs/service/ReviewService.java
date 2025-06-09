package com.wxs.service;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Review;

import java.util.List;

/**
 * 评论服务接口
 * 定义评论相关的业务操作方法
 */
public interface ReviewService {
    
    /**
     * 添加评论
     * 用户对商品进行评价时调用，包含业务验证逻辑
     * @param review 评论对象
     * @return 操作结果
     */
    Result<?> addReview(Review review);
    
    /**
     * 根据商品ID获取评论列表
     * 商品详情页展示评论时调用
     * @param productId 商品ID
     * @return 评论列表
     */
    Result<List<Review>> getReviewsByProductId(Long productId);
    
    /**
     * 根据用户ID获取评论列表
     * 用户查看自己的评论历史时调用
     * @param userId 用户ID
     * @return 评论列表
     */
    Result<List<Review>> getReviewsByUserId(Long userId);
    
    /**
     * 更新评论
     * 用户修改自己的评论时调用，需要验证权限
     * @param review 包含更新信息的评论对象
     * @return 操作结果
     */
    Result<?> updateReview(Review review);
    
    /**
     * 删除评论
     * 用户或管理员删除评论时调用
     * @param id 评论ID
     * @param userId 操作用户ID，用于权限验证
     * @return 操作结果
     */
    Result<?> deleteReview(Long id, Long userId);
    
    /**
     * 获取商品评分统计
     * 返回商品的平均评分和评论总数
     * @param productId 商品ID
     * @return 包含平均评分和评论数的结果
     */
    Result<?> getProductRatingStats(Long productId);
}