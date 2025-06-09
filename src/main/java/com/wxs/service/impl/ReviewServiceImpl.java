package com.wxs.service.impl;

import com.wxs.dao.ReviewMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Review;
import com.wxs.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论服务实现类
 * 实现评论相关的业务逻辑，包括数据验证和业务规则处理
 */
@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewMapper reviewMapper;
    
    /**
     * 添加评论
     * 验证评论数据的有效性，确保评分在合理范围内
     */
    @Override
    @Transactional
    public Result<?> addReview(Review review) {
        try {
            // 验证评分范围，确保在0-5之间
            if (review.getRating() == null || review.getRating() < 0 || review.getRating() > 5) {
                log.warn("评分超出范围: {}", review.getRating());
                return Result.badRequest("评分必须在0-5之间");
            }
            
            // 验证评论内容不能为空
            if (review.getContent() == null || review.getContent().trim().isEmpty()) {
                log.warn("评论内容为空");
                return Result.badRequest("评论内容不能为空");
            }
            
            // 验证用户ID和商品ID不能为空
            if (review.getUserId() == null || review.getProductId() == null) {
                log.warn("用户ID或商品ID为空: userId={}, productId={}", review.getUserId(), review.getProductId());
                return Result.badRequest("用户ID和商品ID不能为空");
            }
            
            int result = reviewMapper.insertReview(review);
            if (result > 0) {
                log.info("评论添加成功: userId={}, productId={}, rating={}", 
                        review.getUserId(), review.getProductId(), review.getRating());
                return Result.success("评论添加成功");
            } else {
                log.error("评论添加失败: {}", review);
                return Result.serverError("评论添加失败");
            }
        } catch (Exception e) {
            log.error("添加评论时发生异常: {}", e.getMessage(), e);
            return Result.serverError("添加评论失败");
        }
    }
    
    /**
     * 根据商品ID获取评论列表
     * 按创建时间倒序排列，最新评论在前
     */
    @Override
    public Result<List<Review>> getReviewsByProductId(Long productId) {
        try {
            if (productId == null) {
                log.warn("商品ID为空");
                return Result.error(Result.BAD_REQUEST,"商品ID不能为空");
            }
            
            List<Review> reviews = reviewMapper.selectByProductId(productId);
            log.info("获取商品评论成功: productId={}, 评论数量={}", productId, reviews.size());
            return Result.success(reviews);
        } catch (Exception e) {
            log.error("获取商品评论时发生异常: productId={}, 错误={}", productId, e.getMessage(), e);
            return Result.error(Result.SERVER_ERROR,"获取评论失败");
        }
    }
    
    /**
     * 根据用户ID获取评论列表
     * 用于用户查看自己的评论历史
     */
    @Override
    public Result<List<Review>> getReviewsByUserId(Long userId) {
        try {
            if (userId == null) {
                log.warn("用户ID为空");
                return Result.error(Result.BAD_REQUEST,"用户ID不能为空");
            }
            
            List<Review> reviews = reviewMapper.selectByUserId(userId);
            log.info("获取用户评论成功: userId={}, 评论数量={}", userId, reviews.size());
            return Result.success(reviews);
        } catch (Exception e) {
            log.error("获取用户评论时发生异常: userId={}, 错误={}", userId, e.getMessage(), e);
            return Result.error(Result.SERVER_ERROR,"获取评论失败");
        }
    }
    
    /**
     * 更新评论
     * 验证用户权限，只允许用户修改自己的评论
     */
    @Override
    @Transactional
    public Result<?> updateReview(Review review) {
        try {
            // 验证评论ID不能为空
            if (review.getId() == null) {
                log.warn("评论ID为空");
                return Result.badRequest("评论ID不能为空");
            }
            
            // 验证评分范围
            if (review.getRating() == null || review.getRating() < 0 || review.getRating() > 5) {
                log.warn("评分超出范围: {}", review.getRating());
                return Result.badRequest("评分必须在0-5之间");
            }
            
            // 验证评论内容不能为空
            if (review.getContent() == null || review.getContent().trim().isEmpty()) {
                log.warn("评论内容为空");
                return Result.badRequest("评论内容不能为空");
            }
            
            // 检查评论是否存在
            Review existingReview = reviewMapper.selectById(review.getId());
            if (existingReview == null) {
                log.warn("评论不存在: id={}", review.getId());
                return Result.notFound("评论不存在");
            }
            
            int result = reviewMapper.updateReview(review);
            if (result > 0) {
                log.info("评论更新成功: id={}, rating={}", review.getId(), review.getRating());
                return Result.success("评论更新成功");
            } else {
                log.error("评论更新失败: {}", review);
                return Result.serverError("评论更新失败");
            }
        } catch (Exception e) {
            log.error("更新评论时发生异常: {}", e.getMessage(), e);
            return Result.serverError("更新评论失败");
        }
    }
    
    /**
     * 删除评论
     * 验证用户权限，确保只能删除自己的评论
     */
    @Override
    @Transactional
    public Result<?> deleteReview(Long id, Long userId) {
        try {
            if (id == null) {
                log.warn("评论ID为空");
                return Result.badRequest("评论ID不能为空");
            }
            
            // 检查评论是否存在并验证权限
            Review existingReview = reviewMapper.selectById(id);
            if (existingReview == null) {
                log.warn("评论不存在: id={}", id);
                return Result.badRequest("评论不存在");
            }
            
            // 验证用户权限，只能删除自己的评论
            if (!existingReview.getUserId().equals(userId)) {
                log.warn("用户无权限删除评论: userId={}, reviewUserId={}", userId, existingReview.getUserId());
                return Result.unauthorized("无权限删除此评论");
            }
            
            int result = reviewMapper.deleteById(id);
            if (result > 0) {
                log.info("评论删除成功: id={}, userId={}", id, userId);
                return Result.success("评论删除成功");
            } else {
                log.error("评论删除失败: id={}", id);
                return Result.serverError("评论删除失败");
            }
        } catch (Exception e) {
            log.error("删除评论时发生异常: id={}, 错误={}", id, e.getMessage(), e);
            return Result.serverError("删除评论失败");
        }
    }
    
    /**
     * 获取商品评分统计
     * 返回平均评分和评论总数，用于商品展示
     */
    @Override
    public Result<?> getProductRatingStats(Long productId) {
        try {
            if (productId == null) {
                log.warn("商品ID为空");
                return Result.badRequest("商品ID不能为空");
            }
            
            Double averageRating = reviewMapper.getAverageRating(productId);
            int reviewCount = reviewMapper.countByProductId(productId);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("averageRating", averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0);
            stats.put("reviewCount", reviewCount);
            
            log.info("获取商品评分统计成功: productId={}, 平均评分={}, 评论数={}", 
                    productId, averageRating, reviewCount);
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取商品评分统计时发生异常: productId={}, 错误={}", productId, e.getMessage(), e);
            return Result.serverError("获取评分统计失败");
        }
    }
}